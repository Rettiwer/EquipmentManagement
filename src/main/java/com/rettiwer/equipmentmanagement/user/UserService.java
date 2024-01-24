package com.rettiwer.equipmentmanagement.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<User> getAllUserWithEmployees() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Collections.singletonList(user);
    }
}
