package com.backend.application.mapper;

import com.backend.application.domain.User;
import com.backend.application.dto.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto CONVERT_TO_DTO(User user);

    Iterable<UserDto> CONVERT_IT_TO_DTO(Iterable<User> users);
}
