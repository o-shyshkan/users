package com.test.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class BirthDaysValidator implements ConstraintValidator<BirthDaysParameters, Object[]> {
    public static final int BEGIN_DATE = 0;
    public static final int END_DATE = 1;

    @Override
    public void initialize(BirthDaysParameters constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext constraintContext) {
        try {
            if ( value[BEGIN_DATE] == null || value[END_DATE] == null ) {
                return false;
            }
            if ( !(value[BEGIN_DATE] instanceof LocalDate firstDate)
                    || !(value[END_DATE] instanceof LocalDate secondDate) ) {
                throw new IllegalArgumentException("Illegal method signature," +
                        " expected two parameters of type Date.");
            }
            return secondDate.isAfter(firstDate);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
