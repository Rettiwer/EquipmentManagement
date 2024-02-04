package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    public final UserRepository userRepository;
    private final AuthenticationService authService;
    public final ItemMapper itemMapper;

    public List<UserItemsDTO> getAllUserItems() {
        User user = authService.getCurrentUser();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN))
            return itemMapper.toUserItemsDtoList(userRepository.findAll());

        List<User> userItems = new ArrayList<>();
        userItems.add(user);
        userItems.addAll(user.getEmployees());

        return itemMapper.toUserItemsDtoList(userItems);
    }

    public UserItemsDTO getSingleUserItemsById(Integer userId) {
        User currentUser = authService.getCurrentUser();
        User requestedUser = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (currentUser.hasRole(Role.UserRole.ROLE_ADMIN))
            return itemMapper.toUserItemsDto(requestedUser);

        if (currentUser.hasRole(Role.UserRole.ROLE_SUPERVISOR) && (Objects.equals(currentUser.getId(), userId) ||
                currentUser.getEmployees().stream().anyMatch(employee -> Objects.equals(employee.getId(), userId))))
            return itemMapper.toUserItemsDto(requestedUser);

        if (!Objects.equals(currentUser.getId(), userId))
            throw new InsufficientPermissionException();

        return itemMapper.toUserItemsDto(requestedUser);
    }
}
