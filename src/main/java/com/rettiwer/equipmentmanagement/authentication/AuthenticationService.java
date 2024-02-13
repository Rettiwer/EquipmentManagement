package com.rettiwer.equipmentmanagement.authentication;

import com.rettiwer.equipmentmanagement.authentication.jwt.JwtService;
import com.rettiwer.equipmentmanagement.authentication.token.Token;
import com.rettiwer.equipmentmanagement.authentication.token.TokenRepository;
import com.rettiwer.equipmentmanagement.authentication.token.TokenType;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.UserMapper;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        var user = userMapper.registerRequestToEntity(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

            var user = userRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(EntityNotFoundException::new);

            var extraClaims = new HashMap<String, Object>();
            extraClaims.put("userId", user.getId());

            var jwtToken = jwtService.generateToken(extraClaims, user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .userId(user.getId())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(EntityNotFoundException::new);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest data, HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new MalformedJwtException("Invalid token.");
        }

        var user = userRepository.findById(data.getUserId()).orElseThrow(EntityNotFoundException::new);

        if (!jwtService.isTokenValid(data.getRefreshToken(), user))
            throw new ExpiredJwtException(null, null, "Token has expired!");

        var extraClaims = new HashMap<String, Object>();
        extraClaims.put("userId", user.getId());

        var accessToken = jwtService.generateToken(extraClaims, user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
