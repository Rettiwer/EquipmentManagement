package com.rettiwer.equipmentmanagement.user.role.mapper;

import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapperResolver {
    private final RoleRepository roleRepository;

    @ObjectFactory
    public Role resolve(RoleDTO roleDTO, @TargetType Class<Role> role) {
        return roleRepository.findByName(Role.UserRole.valueOf(roleDTO.getRole()));
    }
}
