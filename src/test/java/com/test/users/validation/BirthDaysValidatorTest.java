package com.test.users.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BirthDaysValidatorTest {
    private static final String EMAIL_LESS_FOUR_CHAR = "b@y";
    private static final LocalDate [] DATE_RANGE_OK = new LocalDate[] {LocalDate.of(2000,1,1),
            LocalDate.of(2005,1,1)};
    private static final LocalDate [] DATE_RANGE_NULL = new LocalDate[] {null,null};
    private static final LocalDate [] DATE_RANGE_NOT_OK = new LocalDate[] {LocalDate.of(2010,1,1),
            LocalDate.of(2005,1,1)};
    private static final String [] DATE_TYPE_WRONG = new String[] {"null","null"};
    private static final String ILLEGAL_METHOD_SIGNATURE_EXPECTED = "Illegal method signature," +
            " expected two parameters of type Date.";
    private ConstraintValidatorContext constraintValidatorContext;
    private BirthDaysValidator birthDaysValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        birthDaysValidator = new BirthDaysValidator();
    }

    @Test
    void isValid_Ok() {
        boolean actual = birthDaysValidator.isValid(DATE_RANGE_OK, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void  isValid_birthDaysNull_notOk() {
        boolean actual = birthDaysValidator.isValid(DATE_RANGE_NULL, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_firstBiggerThanSecond_notOk() {
        boolean actual = birthDaysValidator.isValid(DATE_RANGE_NOT_OK, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_dateType_notOk() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            birthDaysValidator.isValid(DATE_TYPE_WRONG, constraintValidatorContext));
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(ILLEGAL_METHOD_SIGNATURE_EXPECTED));
    }
}
