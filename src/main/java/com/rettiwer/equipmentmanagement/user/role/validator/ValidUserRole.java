package com.rettiwer.equipmentmanagement.user.role.validator;

import com.rettiwer.equipmentmanagement.user.role.validator.UserRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleValidator.class)
public @interface ValidUserRole {
    String message() default "Invalid user role value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
