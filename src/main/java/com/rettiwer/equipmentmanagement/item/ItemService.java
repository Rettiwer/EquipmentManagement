package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.invoice.InvoiceRepository;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.UserMapper;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public UserItemsDTO getAllUserItemsById(Integer userId) {
        User currentUser = authService.getCurrentUser();
        User requestedUser = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN) ||
                !currentUser.getId().equals(userId) ||
                currentUser.getEmployees().stream().noneMatch(employee -> employee.getId().equals(userId)))
            throw new InsufficientPermissionException();

        return itemMapper.toUserItemsDto(requestedUser);
    }
}
