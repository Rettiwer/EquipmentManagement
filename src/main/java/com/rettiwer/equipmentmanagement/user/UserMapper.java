package com.rettiwer.equipmentmanagement.user;


import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.mapper.RoleMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class})
public interface UserMapper {

    User idToEntity(Integer id);

    @Mapping(target = "supervisorId", source = "supervisor.id")
    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);

    @Mapping(target = "supervisorId", source = "supervisor.id")
    UserEmployeesDTO toUserEmployeesDto(User user);

    @Mapping(target = "supervisor", source = "supervisorId")
    User toEntity(UserDTO userDTO);

    List<UserDTO> toUserDtoList(List<User> users);

    List<UserEmployeesDTO> toUserEmployeesDtoList(List<User> users);

    @Mapping(source = "supervisorId", target = "supervisor")
    User registerRequestToEntity(RegisterRequest request);
}
