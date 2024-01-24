package com.rettiwer.equipmentmanagement.user.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(Role.UserRole name);
    List<Role> findByNameIn(List<String> name);
}
