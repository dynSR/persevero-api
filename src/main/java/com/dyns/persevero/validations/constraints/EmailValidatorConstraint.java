package com.dyns.persevero.validations.constraints;

import com.dyns.persevero.utils.Validate;
import com.dyns.persevero.validations.validators.EmailValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidatorConstraint implements ConstraintValidator<EmailValidator, String> {

    @Override
    public void initialize(EmailValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return Validate.isEmailValid(email);
    }
}
