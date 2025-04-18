package com.dyns.persevero.validations.constraints;

import com.dyns.persevero.validations.validators.EnumValidator;
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
    public boolean isValid(String potentialEnumValue, ConstraintValidatorContext constraintValidatorContext) {
        if (potentialEnumValue == null) return false;
        return Arrays
                .stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .anyMatch(value -> value.equals(potentialEnumValue));
    }

}
