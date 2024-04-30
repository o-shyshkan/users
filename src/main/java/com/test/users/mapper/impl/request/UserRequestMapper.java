package com.test.users.mapper.impl.request;

import com.test.users.mapper.DtoRequestMapper;
import com.test.users.model.User;
import com.test.users.model.dto.request.UserRequestDto;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper implements DtoRequestMapper<UserRequestDto, User> {
    @Override
    public User fromDto(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }
}
