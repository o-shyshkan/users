package com.test.users.mapper;

import com.test.users.model.User;
import com.test.users.model.dto.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    UserResponseDto toDto(User user);
}
