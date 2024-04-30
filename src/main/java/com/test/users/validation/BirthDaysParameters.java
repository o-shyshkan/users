package com.test.users.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = BirthDaysValidator.class)
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDaysParameters {
    String message() default "Invalid date of birthday";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
 }

