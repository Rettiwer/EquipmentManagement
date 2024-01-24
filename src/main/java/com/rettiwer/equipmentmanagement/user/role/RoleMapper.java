package com.rettiwer.equipmentmanagement.user.role;

import org.mapstruct.*;
import java.util.List;



@Mapper(componentModel = "spring", uses = {RoleMapperResolver.class})
public interface RoleMapper {
    Role toEntity(String name);

    List<Role> toRoles(List<String> roleNames);
}
