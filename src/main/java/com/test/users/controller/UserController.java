package com.test.users.controller;

import com.test.users.mapper.RequestMapper;
import com.test.users.mapper.ResponseMapper;
import com.test.users.service.UserService;
import com.test.users.util.CalculateAge;
import com.test.users.util.DateTimePatternUtil;
import com.test.users.validation.BirthDaysParameters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    public static final String MESSAGE_RESTRICTION_BY_AGE = "There is restriction by age : ";
    private final RequestMapper userRequestMapper;
    private final ResponseMapper userResponseMapper;
    private final UserService userService;
    @Value("${application.allowed_age_registration}")
    private int ageLimit;

    @PostMapping("/add")
    public Map<String, List<UserResponseDto>> add(@RequestBody @Valid UserRequestDto userRequestDto) {
        if (CalculateAge.getAgeFromBirthDate(userRequestDto.getBirthDate()) <= ageLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MESSAGE_RESTRICTION_BY_AGE + ageLimit);
        }
        User user = userService.add(userRequestMapper.fromDto(userRequestDto));
        return Map.of("data", List.of(userResponseMapper.toDto(user)));
    }

    @PatchMapping("/{id}")
    public Map<String, List<UserResponseDto>> updatePatch(@PathVariable Long id,
                       @RequestBody @Valid UserRequestDto userRequestDto) {
        return Map.of("data", List.of(userResponseMapper.toDto(userService.updatePartial(userRequestDto, id))));
    }

    @PutMapping("/{id}")
    public Map<String, List<UserResponseDto>> updatePut(@PathVariable Long id,
                       @RequestBody @Valid UserRequestDto userRequestDto) {
        User user = userRequestMapper.fromDto(userRequestDto);
        user.setId(id);
        return Map.of("data", List.of(userResponseMapper.toDto(userService.update(user))));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.remove(id);
    }

    @GetMapping("/birth-date")
    @BirthDaysParameters
    public Map<String, List<UserResponseDto>> findBirthDateRange(
            @RequestParam @NotNull @DateTimeFormat(pattern = DateTimePatternUtil.DATE_PATTERN) LocalDate beginDate,
            @RequestParam @NotNull @DateTimeFormat(pattern = DateTimePatternUtil.DATE_PATTERN) LocalDate endDate) {
        return Map.of("data",userService.findUserByBirthDateRange(beginDate, endDate).stream()
                .map(userResponseMapper::toDto)
                .collect(Collectors.toList()));
    }
}
