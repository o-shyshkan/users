package com.test.users.mapper;

import com.test.users.model.User;
import com.test.users.model.dto.request.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {
    User fromDto(UserRequestDto dto);

    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);
}

