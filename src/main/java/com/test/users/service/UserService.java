package com.test.users.service;

import com.test.users.model.User;
import com.test.users.model.dto.request.UserRequestDto;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user);

    User updatePartial(UserRequestDto userRequestDto, Long id);

    void remove(Long id);

    List<User>findUserByBirthDateRange(LocalDate beginDate, LocalDate endDate);
}
