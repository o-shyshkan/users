package com.test.users.util;

import java.time.LocalDate;

public class CalculateAge {
    public CalculateAge() {
    }

    public int getAgeFromBirthDate(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}