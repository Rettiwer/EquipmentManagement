package com.rettiwer.equipmentmanagement.user.role.mapper;

import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapperResolver.class})
public interface RoleMapper {
    RoleDTO toDto(Role role);
    Role toEntity(RoleDTO roleDTO);
    List<Role> toRoles(List<RoleDTO> roleDTOS);
}
