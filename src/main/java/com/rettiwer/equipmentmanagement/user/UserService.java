package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUserWithEmployees() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
        }

        return List.of(userMapper.toDto(user));
    }
}
