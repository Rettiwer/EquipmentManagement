package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.exception.UserHasEmployeesException;
import com.rettiwer.equipmentmanagement.user.exception.UserHasItemsException;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

@Slf4j
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

        return userMapper.toDto(userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<UserEmployeesDTO> getAllUsers() {
        User user = authService.getCurrentUser().orElseThrow();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userMapper.toUserEmployeesDtoList(userRepository.findAll());
        }

        return List.of(userMapper.toUserEmployeesDto(user));
    }

    @Transactional
    public UserDTO create(RegisterRequest registerRequest) {
        User currentUser = authService.getCurrentUser().orElseThrow();

        //Only ROLE_ADMIN can insert employee for different supervisor
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) ||
                    !registerRequest.getSupervisorId().equals(currentUser.getId())) {
                throw new InsufficientPermissionException();
            }
        }

        var request = userMapper.registerRequestToEntity(registerRequest);

        return userMapper.toDto(userRepository.save(request));
    }

    @Transactional
    public UserDTO replace(UserDTO userDTO, Integer userId) {
        User currentUser = authService.getCurrentUser().orElseThrow();

        //Only ROLE_ADMIN can change employee for different supervisor
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) || !userDTO.getSupervisorId().equals(currentUser.getId())) {
                throw new InsufficientPermissionException();
            }
        }

        var request = userMapper.toEntity(userDTO);

        return userMapper.toDto(userRepository.findById(userId).map(user -> {
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setEmail(request.getEmail());
            user.setRoles(request.getRoles());

            if (request.getPassword() != null)
                user.setPassword(passwordEncoder.encode(request.getPassword()));

            if (request.getSupervisor() != null &&
                    !Objects.equals(request.getSupervisor().getId(), user.getSupervisor().getId()))
                user.setSupervisor(userRepository.findById(request.getSupervisor().getId())
                        .orElseThrow(() -> new EntityNotFoundException("There is no supervisor with this id.")));

            return userRepository.save(user);
        }).orElseThrow(EntityNotFoundException::new));
    }

    public void delete(Integer userId) {
        User user = authService.getCurrentUser().orElseThrow();

        User toDeleteUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

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
