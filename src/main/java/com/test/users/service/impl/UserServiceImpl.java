package com.test.users.service.impl;

import com.test.users.model.User;
import com.test.users.service.UserService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User add(User user) {
        //add user throughout repository to DB
        user.setId(1L);
        return user;
    }

    @Override
    public void remove(Long id) {
        //delete user throughout repository to DB
    }

    @Override
    public User update(User user) {
        //update user throughout repository to DB
        return user;
    }

    @Override
    public List<User> findUserByBirthDateRange(LocalDate beginDate, LocalDate endDate) {
        // find users who have birthday in range started dateBegin and finished dateEnd
        return getExampleListUser();
    }

    private List<User> getExampleListUser() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("1234@gmail.com");
        bob.setFirstName("Bob");
        bob.setLastName("Bird");
        bob.setBirthDate(LocalDate.of(1985,1,1));
        User peter = new User();
        peter.setId(2L);
        peter.setEmail("abcd@gmail.com");
        peter.setFirstName("Peter");
        peter.setLastName("Apple");
        peter.setBirthDate(LocalDate.of(2000,1,1));
        return List.of(bob, peter);
    }
}
