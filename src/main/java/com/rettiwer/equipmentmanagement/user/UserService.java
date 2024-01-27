package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public Object getAllUserWithEmployees() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userMapper.toUserDtoList(userRepository.findAll());
        }

        return List.of(userMapper.toDto(user));
    }
}
