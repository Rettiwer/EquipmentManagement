package com.rettiwer.equipmentmanagement.item.validator;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class SupervisorEmployeeValidator implements ConstraintValidator<IsSupervisorEmployee, Integer> {
    private final AuthenticationService authService;

    @Override
    public void initialize(IsSupervisorEmployee constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) && !currentUser.hasRole(Role.UserRole.ROLE_ADMIN)) {
            return Objects.equals(currentUser.getId(), value) ||
                    currentUser.getEmployees().stream().anyMatch(user -> user.getId().equals(value));
        }

        return true;
    }
}