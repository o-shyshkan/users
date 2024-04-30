package com.test.users.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String EMAIL_WITH_OUT_DOT = "bchupika@gmail";
    private static final String EMAIL_WITH_OUT_A = "123456.gmail";
    private static final String EMAIL_LESS_FOUR_CHAR = "b@y";

    private ConstraintValidatorContext constraintValidatorContext;
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_Ok() {
        boolean actual = emailValidator.isValid(EMAIL, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_emailNull_Ok() {
        boolean actual = emailValidator.isValid(null, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_witOutDot_notOk() {
        boolean actual = emailValidator.isValid(EMAIL_WITH_OUT_DOT, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_witOutA_notOk() {
        boolean actual = emailValidator.isValid(EMAIL_WITH_OUT_A, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_lessFourChar_notOk() {
        boolean actual = emailValidator.isValid(EMAIL_LESS_FOUR_CHAR, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}