package com.rettiwer.equipmentmanagement.user.role;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@RequiredArgsConstructor
public class RoleMapperResolver {
    private final RoleRepository roleRepository;

    @ObjectFactory
    public Role resolve(String name, @TargetType Class<Role> role) {
        try {
            return roleRepository.findByName(Role.UserRole.valueOf(name));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Role definition not found");
        }
    }

}
