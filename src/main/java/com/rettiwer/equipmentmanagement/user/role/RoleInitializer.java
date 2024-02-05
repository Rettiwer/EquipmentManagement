package com.rettiwer.equipmentmanagement.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if(roleRepository.findAll().isEmpty()) {
            List<Role> roles = Arrays.stream(Role.UserRole.values())
                    .map(Role::new).toList();

            roleRepository.saveAll(roles);
        }
    }
}
