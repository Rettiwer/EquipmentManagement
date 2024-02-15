package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.apierror.exception.RelationConstraintViolationException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getById(Integer id) {
        User user = authService.getCurrentUser();

        if (user.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)) &&
                !Objects.equals(user.getId(), id)) {
            throw new InsufficientPermissionException();
        }

        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<UserEmployeesDTO> getAllUsers() {
        User user = authService.getCurrentUser();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return userMapper.toUserEmployeesDtoList(userRepository.findAll());
        }

        return List.of(userMapper.toUserEmployeesDto(user));
    }

    public List<BasicUserDTO> searchByName(String text) {
        User user = authService.getCurrentUser();

        if (!user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            throw new InsufficientPermissionException();
        }

        return userMapper.toBasicUserDtoList(userRepository
                .findFullTextSearchFullName(text));
    }

    @Transactional
    public UserDTO create(RegisterRequest registerRequest) {
        User currentUser = authService.getCurrentUser();

        //Only ROLE_ADMIN can insert employee for different supervisor
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) ||
                    !registerRequest.getSupervisorId().equals(currentUser.getId())) {
                throw new InsufficientPermissionException();
            }

            if (registerRequest.getRoles().stream().anyMatch(roleDTO ->
                    roleDTO.getName().equals(Role.UserRole.ROLE_ADMIN.name) ||
                            roleDTO.getName().equals(Role.UserRole.ROLE_SUPERVISOR.name))) {
                throw new InsufficientPermissionException();
            }
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        var request = userMapper.registerRequestToEntity(registerRequest);

        return userMapper.toUserDto(userRepository.save(request));
    }

    @Transactional
    public UserDTO replace(UserDTO userDTO, Integer userId) {
        User currentUser = authService.getCurrentUser();

        //Only ROLE_ADMIN can change employee for different supervisor
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) || !userDTO.getSupervisorId().equals(currentUser.getId())) {
                throw new InsufficientPermissionException();
            }

            if (userDTO.getRoles().stream().anyMatch(roleDTO ->
                    roleDTO.getName().equals(Role.UserRole.ROLE_ADMIN.name) ||
                            roleDTO.getName().equals(Role.UserRole.ROLE_SUPERVISOR.name))) {
                throw new InsufficientPermissionException();
            }
        }

        return userMapper.toUserDto(userRepository.findById(userId).map(user -> {
            User updatedUser = userMapper.updateEntity(userDTO, user);

            if (updatedUser.getPassword() != null) {
                updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            if (updatedUser.getSupervisor() != null) {
                if (Objects.equals(updatedUser.getSupervisor().getId(), user.getId()))
                    throw new RelationConstraintViolationException("Supervisor cannot be his own superior.");

                var supervisor = userRepository.findById(updatedUser.getSupervisor().getId())
                        .orElseThrow(() -> new EntityNotFoundException("There is no supervisor with this id."));

                if (!supervisor.hasRole(Role.UserRole.ROLE_SUPERVISOR))
                    throw new RelationConstraintViolationException("Supplied user for supervisor does not have role supervisor.");

                updatedUser.setSupervisor(supervisor);
            }

            return userRepository.save(user);
        }).orElseThrow(EntityNotFoundException::new));
    }

    public void delete(Integer userId) {
        User user = authService.getCurrentUser();

        User toDeleteUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        if (!user.hasRole(Role.UserRole.ROLE_ADMIN)) {
            if (!user.hasRole(Role.UserRole.ROLE_SUPERVISOR) ||
                    !toDeleteUser.getSupervisor().getId().equals(user.getId())) {
                throw new InsufficientPermissionException();
            }
        }

        if (!toDeleteUser.getEmployees().isEmpty())
            throw new RelationConstraintViolationException("USER_HAS_ASSIGNED_EMPLOYEES_REASSIGN_THEM");

        if (!toDeleteUser.getItems().isEmpty())
            throw new RelationConstraintViolationException("USER_HAS_ASSIGNED_ITEMS_REASSIGN_THEM");

        userRepository.delete(toDeleteUser);
    }
}
