package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.exception.UserHasEmployeesException;
import com.rettiwer.equipmentmanagement.user.exception.UserHasItemsException;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUserById(Integer id) {
        User user = authService.getCurrentUser().orElseThrow();

        if (!user.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)) &&
                !Objects.equals(user.getId(), id)) {
            throw new InsufficientPermissionException();
        }

        return userMapper.toDto(userRepository.findById(id).orElseThrow());
    }

    public List<UserEmployeesDTO> getAllUsers() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userMapper.toUserEmployeesDtoList(userRepository.findAll());
        }

        return List.of(userMapper.toUserEmployeesDto(user));
    }

    @Transactional
    public UserDTO replaceOrInsert(UserDTO userDTO, @Nullable User user) {
        User currentUser = authService.getCurrentUser().orElseThrow();

        //Only ROLE_ADMIN can insert employee for different supervisor
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) || !userDTO.getSupervisorId().equals(currentUser.getId())) {
                throw new InsufficientPermissionException();
            }
        }

        if (user == null)
            userDTO.setId(null);

        var request = userMapper.toEntity(userDTO);

        if (userDTO.getPassword() != null)
            request.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userMapper.toDto(userRepository.save(request));
    }

    public void delete(User toDeleteUser) {
        User user = authService.getCurrentUser().orElseThrow();
        if (!user.hasRole(Role.UserRole.ROLE_ADMIN) &&
                (user.hasRole(Role.UserRole.ROLE_SUPERVISOR) &&
                        toDeleteUser.getSupervisor().getId().equals(user.getId())))
            throw new InsufficientPermissionException();

        if (!toDeleteUser.getEmployees().isEmpty())
            throw new UserHasEmployeesException();

        if (!toDeleteUser.getItems().isEmpty())
            throw new UserHasItemsException();

        userRepository.delete(toDeleteUser);
    }
}
