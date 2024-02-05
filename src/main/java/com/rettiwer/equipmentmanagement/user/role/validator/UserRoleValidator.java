package com.rettiwer.equipmentmanagement.user.role.validator;

import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<ValidateUserRole, String> {
    @Override
    public void initialize(ValidateUserRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull or @NotBlank
        }

        try {
            Role.UserRole.valueOf(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}