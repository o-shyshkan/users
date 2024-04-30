package com.test.users.model.dto.request;


import com.test.users.validation.Email;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRequestDto {
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
