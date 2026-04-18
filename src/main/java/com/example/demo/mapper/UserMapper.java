package com.example.demo.mapper;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);

    List<UserDto> toDtoList(List<User> users);
}
