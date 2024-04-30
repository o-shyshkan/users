package com.test.users.mapper;

public interface DtoRequestMapper<D, C> {
    C fromDto(D dto);
}
