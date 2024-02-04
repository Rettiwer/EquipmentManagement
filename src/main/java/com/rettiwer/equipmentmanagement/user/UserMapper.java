package com.rettiwer.equipmentmanagement.user;


import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.item.Item;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.item.ItemMapper;
import com.rettiwer.equipmentmanagement.user.role.mapper.RoleMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoleMapper.class})
public interface UserMapper {

    User idToEntity(Integer id);

    @Mapping(target = "supervisorId", source = "supervisor.id")
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDto(User user);

    @Mapping(target = "supervisor", source = "supervisorId")
    User toEntity(UserDTO userDTO);

    @Mapping(target = "supervisor", source = "supervisorId")
    User updateEntity(UserDTO userDTO, @MappingTarget User user);

    List<UserDTO> toUserDtoList(List<User> users);

    /*

        User employees mapping

     */

    @Mapping(target = "supervisorId", source = "supervisor.id")
    UserEmployeesDTO toUserEmployeesDto(User user);

    List<UserEmployeesDTO> toUserEmployeesDtoList(List<User> users);


    /*

        Register form mapping

     */

    @Mapping(source = "supervisorId", target = "supervisor")
    User registerRequestToEntity(RegisterRequest request);
}
