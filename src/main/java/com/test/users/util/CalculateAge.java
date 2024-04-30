package com.test.users.util;

import java.time.LocalDate;

public class CalculateAge {
    public static int getAgeFromBirthDate(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}