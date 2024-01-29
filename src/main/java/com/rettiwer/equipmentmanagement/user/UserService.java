package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public UserDTO getUserById(Integer id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow());
    }

    public Object getAllUserWithEmployees() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userMapper.toUserDtoList(userRepository.findAll());
        }

        return List.of(userMapper.toDto(user));
    }

    public List<UserDTO> getAllUsersWithItems() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN))
            return userMapper.toUserDtoList(userRepository.findAll());


        if (user.hasRole(Role.UserRole.ROLE_SUPERVISOR))
            return userMapper.toUserDtoList(userRepository.findAll());

        return List.of(userMapper.toDto(user));
    }

    @Transactional
    public UserDTO insert(RegisterRequest request) {
        User user = authService.getCurrentUser().orElseThrow();
        if (!user.hasRole(Role.UserRole.ROLE_ADMIN))
            throw new InsufficientPermissionException();

        var userRequest = userMapper.registerRequestToEntity(request);

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return userMapper.toDto(userRepository.save(userRequest));
    }
}
