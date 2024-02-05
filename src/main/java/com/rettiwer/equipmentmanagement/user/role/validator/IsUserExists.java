package com.rettiwer.equipmentmanagement.user.role.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserExistsValidator.class)
public @interface IsUserExists {
    String message() default "User of this id does not exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}