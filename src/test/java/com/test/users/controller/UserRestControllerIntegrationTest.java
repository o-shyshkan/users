package com.test.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.users.mapper.impl.request.UserRequestMapper;
import com.test.users.mapper.impl.response.UserResponseMapper;
import com.test.users.model.User;
import com.test.users.model.dto.request.UserRequestDto;
import com.test.users.model.dto.response.UserResponseDto;
import com.test.users.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
public class UserRestControllerIntegrationTest {

    public static final String EXPECTED_RESTRICTION_BY_AGE = "There is restriction by age : ";
    public static final String EXPECTED_EMAIL_SHOULD_BE_VALID = "{\"errors\":[\"Email should be valid\"]";
    public static final String URL_USERS_ID = "/users/{id}";
    public static final String URL_USER_ADD = "/users/add";
    public static final String URL_BIRTH_DATE_WRONG_ORDER = "/users/birth-date?beginDate=01.01.2010&endDate=01.01.2005";
    public static final String URL_BIRTH_DATE_CORRECT_ORDER = "/users/birth-date?beginDate=01.01.2000&endDate=01.01.2005";
    public static final long BOB_ID = 1L;
    public static final long PETER_ID = 2L;
    public static final long ALICE_ID = 3L;
    public static final String $_ID = "$.id";
    public static final String $_EMAIL = "$.email";
    public static final String $_FIRST_NAME = "$.firstName";
    public static final String $_LAST_NAME = "$.lastName";
    public static final String $_BIRTH_DATE = "$.birthDate";
    public static final String WRONG_EMAIL = "@gmail.com";
    public static final String $_SIZE = "$.size()";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRequestMapper userDtoRequestMapper;
    @MockBean
    private UserResponseMapper userDtoResponseMapper;
    @Value("${application.allowed_age_registration}")
    private int ageLimit;
    private User bob;
    private User peter;
    private UserResponseDto bobDto;

    @BeforeEach
    public void setup(){
        bob = new User();
        bob.setId(BOB_ID);
        bob.setEmail("1234@gmail.com");
        bob.setFirstName("Bob");
        bob.setLastName("Bird");
        bob.setBirthDate(LocalDate.of(1985,1,1));
        peter = new User();
        peter.setId(PETER_ID);
        peter.setEmail("abcd@gmail.com");
        peter.setFirstName("Peter");
        peter.setLastName("Apple");
        peter.setBirthDate(LocalDate.of(2000,1,1));
        bobDto = new UserResponseDto();
        bobDto.setId(BOB_ID);
        bobDto.setEmail("1234@gmail.com");
        bobDto.setFirstName("Bob");
        bobDto.setLastName("Bird");
        bobDto.setBirthDate(LocalDate.of(1985,1,1));
    }

    @Test
    public void givenUser_whenPostUser_thenReturn200() throws Exception {
        when(userDtoRequestMapper.fromDto(any(UserRequestDto.class))).thenReturn(bob);
        when(userService.add(any(User.class))).thenReturn(bob);
        when(userDtoResponseMapper.toDto(bob)).thenReturn(bobDto);
        mvc.perform(post(URL_USER_ADD).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath($_ID).value(bob.getId()))
                .andExpect(jsonPath($_EMAIL).value(bob.getEmail()))
                .andExpect(jsonPath($_FIRST_NAME).value(bob.getFirstName()))
                .andExpect(jsonPath($_LAST_NAME).value(bob.getLastName()))
                .andExpect(jsonPath($_BIRTH_DATE).value(bob.getBirthDate().toString()))
                .andDo(print());
    }

    @Test
    public void givenUser_whenPostUserWithAgeBelowLimit_thenReturn400() throws Exception {
        bob.setBirthDate(LocalDate.of(LocalDate.now().getYear()-ageLimit,1,1));
        mvc.perform(post(URL_USER_ADD).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bob)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals(EXPECTED_RESTRICTION_BY_AGE
                        + ageLimit, result.getResponse().getErrorMessage()))
                .andDo(print());
    }

    @Test
    public void givenUser_whenPostUserWithWrongEmail_thenReturn400() throws Exception {
        bob.setEmail(WRONG_EMAIL);
        mvc.perform(post(URL_USER_ADD).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bob)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals(EXPECTED_EMAIL_SHOULD_BE_VALID + "}",
                        result.getResponse().getContentAsString()))
                .andDo(print());
    }

    @Test
    public void givenUserId_whenPatchUser_thenReturn200() throws Exception {
        User alice = new User();
        alice.setId(ALICE_ID);
        alice.setLastName("Pink");
        when(userDtoRequestMapper.fromDto(any(UserRequestDto.class))).thenReturn(alice);
        when(userService.update(alice)).thenReturn(alice);
        mvc.perform(patch(URL_USERS_ID, ALICE_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath($_ID).value(alice.getId()))
                .andExpect(jsonPath($_LAST_NAME).value(alice.getLastName()))
                .andDo(print());
    }

    @Test
    public void givenUserId_whenPutUser_thenReturn200() throws Exception {
        when(userDtoRequestMapper.fromDto(any(UserRequestDto.class))).thenReturn(bob);
        when(userService.update(bob)).thenReturn(bob);
        mvc.perform(patch(URL_USERS_ID, BOB_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath($_ID).value(bob.getId()))
                .andExpect(jsonPath($_EMAIL).value(bob.getEmail()))
                .andExpect(jsonPath($_FIRST_NAME).value(bob.getFirstName()))
                .andExpect(jsonPath($_LAST_NAME).value(bob.getLastName()))
                .andExpect(jsonPath($_BIRTH_DATE).value(bob.getBirthDate().toString()))
                .andDo(print());
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturnNoContent() throws Exception {
        doNothing().when(userService).remove(BOB_ID);
        mvc.perform(delete(URL_USERS_ID, BOB_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void givenUserId_whenDeleteUserAndException_thenReturn500() throws Exception {
        doThrow(Exception.class).when(userService);
        mvc.perform(delete(URL_USERS_ID, BOB_ID))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    public void givenBirthdayRange_whenGetUsersByBirthdayRange_thenReturnJsonUsers() throws Exception {
        List<User> expectedUsers = Arrays.asList(bob,peter);
        when(userService.findUserByBirthDateRange(
                LocalDate.of(2000,1,1),
                LocalDate.of(2005,1,1))).thenReturn(expectedUsers);
        mvc.perform(get(URL_BIRTH_DATE_CORRECT_ORDER))
                .andExpect(status().isOk())
                .andExpect(jsonPath($_SIZE).value(expectedUsers.size()))
                .andDo(print());
    }

    @Test
    public void givenBirthdayRange_whenGetUsersByBirthdayIncorrectRange_thenReturn400() throws Exception {
        mvc.perform(get(URL_BIRTH_DATE_WRONG_ORDER))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void givenBirthdayRange_whenGetUsersByBirthdayIncorrectRange_thenGenerateConstraintViolationException()
            throws Exception {
        mvc.perform(get(URL_BIRTH_DATE_WRONG_ORDER))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(ConstraintViolationException.class,
                        result.getResolvedException()))
                .andDo(print());
    }
}
