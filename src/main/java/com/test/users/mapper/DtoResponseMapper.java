package com.test.users.mapper;

public interface DtoResponseMapper<D, C> {
    D toDto(C object);
}
