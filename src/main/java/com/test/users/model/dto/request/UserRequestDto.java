package com.test.users.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRequestDto {
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;
    @Past
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
