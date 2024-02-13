package com.rettiwer.equipmentmanagement.user.role.validator;

import com.rettiwer.equipmentmanagement.user.BasicUserDTO;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserExistsValidator implements ConstraintValidator<IsUserExists, BasicUserDTO> {
    private final UserRepository userRepository;

    @Override
    public void initialize(IsUserExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(BasicUserDTO value, ConstraintValidatorContext context) {
        return userRepository.existsById(value.getId());
    }
}