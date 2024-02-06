package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import com.rettiwer.equipmentmanagement.user.role.DefaultRoleInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final DefaultRoleInitializer defaultRoleInitializer;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void run(String... args) {
        defaultRoleInitializer.run();

        if (userRepository.findAll().isEmpty()) {
            var request = new RegisterRequest(
                    "Super",
                    "Admin",
                    "superadmin@example.com",
                    passwordEncoder.encode("SuperSecret0!"),
                    List.of(new RoleDTO("ROLE_ADMIN")),
                    null);

            var newUser = userMapper.registerRequestToEntity(request);

            userRepository.save(newUser);
        }
    }
}
