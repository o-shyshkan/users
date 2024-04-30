package com.test.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class BirthDaysValidator implements ConstraintValidator<BirthDaysParameters, Object[]> {

    @Override
    public void initialize(BirthDaysParameters constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext constraintContext) {
        try {
            if ( value[0] == null || value[1] == null ) {
                return false;
            }
            if ( !(value[0] instanceof LocalDate firstDate)
                    || !(value[1] instanceof LocalDate secondDate) ) {
                throw new IllegalArgumentException("Illegal method signature," +
                        " expected two parameters of type Date.");
            }
            return secondDate.isAfter(firstDate);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
