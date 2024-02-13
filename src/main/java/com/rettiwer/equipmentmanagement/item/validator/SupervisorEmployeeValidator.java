package com.rettiwer.equipmentmanagement.item.validator;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.user.BasicUserDTO;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class SupervisorEmployeeValidator implements ConstraintValidator<IsSupervisorEmployee, BasicUserDTO> {
    private final AuthenticationService authService;

    @Override
    public void initialize(IsSupervisorEmployee constraintAnnotation) {
    }

    @Override
    public boolean isValid(BasicUserDTO value, ConstraintValidatorContext context) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) && !currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return Objects.equals(currentUser.getId(), value.getId()) ||
                    currentUser.getEmployees().stream().anyMatch(user -> user.getId().equals(value.getId()));
        }

        return true;
    }
}