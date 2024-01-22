package com.rettiwer.equipmentmanagement.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
