package com.test.users.service;

import com.test.users.model.User;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user);

    void remove(Long id);

    List<User>findUserByBirthDateRange(LocalDate beginDate, LocalDate endDate);
}
