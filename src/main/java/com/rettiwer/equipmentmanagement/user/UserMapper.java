package com.rettiwer.equipmentmanagement.user;


import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.mapper.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoleMapper.class})
public interface UserMapper {

    User idToEntity(Integer id);

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    List<UserDTO> toUserDtoList(List<User> users);

    User registerRequestToEntity(RegisterRequest request);
}
