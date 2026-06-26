package com.example.genetiicz.PasswordValidator;

import com.example.genetiicz.DTO.ResetPasswordDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches,ResetPasswordDTO> {

    @Override
    public boolean isValid(ResetPasswordDTO dto, ConstraintValidatorContext context) {
        return dto.password() != null &&
                dto.password().equals(dto.samePassword());
    }
}