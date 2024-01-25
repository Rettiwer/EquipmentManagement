package com.rettiwer.equipmentmanagement.user.role.mapper;

import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapperResolver.class})
public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);
    List<Role> toRoles(List<RoleDTO> roleDTOS);
}
