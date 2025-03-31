package com.dyns.persevero.validation.constraint;

import com.dyns.persevero.validation.validator.EnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(
            String s,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        if (s == null) return false;

        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .anyMatch(v -> v.equals(s));
    }
}
