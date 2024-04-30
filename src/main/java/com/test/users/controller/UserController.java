package com.test.users.controller;

import com.test.users.mapper.DtoRequestMapper;
import com.test.users.mapper.DtoResponseMapper;
import com.test.users.service.UserService;
import com.test.users.util.DateTimePatternUtil;
import com.test.users.validation.BirthDaysParameters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.test.users.model.dto.response.UserResponseDto;
import com.test.users.model.dto.request.UserRequestDto;
import com.test.users.model.User;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    public static final String MESSAGE_RESTRICTION_BY_AGE = "There is restriction by age : ";
    private final DtoRequestMapper<UserRequestDto, User> userDtoRequestMapper;
    private final DtoResponseMapper<UserResponseDto, User> userDtoResponseMapper;
    private final UserService userService;
    @Value("${application.allowed_age_registration}")
    private int ageLimit;

    @PostMapping("/add")
    public UserResponseDto add(@RequestBody @Valid UserRequestDto userRequestDto) {
        if (getAgeFromBirthDate(userRequestDto.getBirthDate()) <= ageLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_RESTRICTION_BY_AGE + ageLimit);
        }
        User user = userService.add(userDtoRequestMapper.fromDto(userRequestDto));
        return userDtoResponseMapper.toDto(user);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updatePatch(@PathVariable Long id,
                       @RequestBody @Valid UserRequestDto userRequestDto) {
        User user = userDtoRequestMapper.fromDto(userRequestDto);
        user.setId(id);
        return userDtoResponseMapper.toDto(userService.update(user));
    }

    @PutMapping("/{id}")
    public UserResponseDto updatePut(@PathVariable Long id,
                       @RequestBody @Valid UserRequestDto userRequestDto) {
        User user = userDtoRequestMapper.fromDto(userRequestDto);
        user.setId(id);
        return userDtoResponseMapper.toDto(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        try {
            userService.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/birth-date")
    @BirthDaysParameters
    public List<UserResponseDto> findBirthDateRange(
            @RequestParam @NotNull @DateTimeFormat(pattern = DateTimePatternUtil.DATE_PATTERN) LocalDate beginDate,
            @RequestParam @NotNull @DateTimeFormat(pattern = DateTimePatternUtil.DATE_PATTERN) LocalDate endDate) {
        return userService.findUserByBirthDateRange(beginDate, endDate).stream()
                .map(userDtoResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    private int getAgeFromBirthDate(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
