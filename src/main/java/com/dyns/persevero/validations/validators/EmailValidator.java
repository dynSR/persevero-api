package com.dyns.persevero.validations.validators;

import com.dyns.persevero.validations.constraints.EmailValidatorConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmailValidatorConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface EmailValidator {

    String message() default "Provided email format in invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
