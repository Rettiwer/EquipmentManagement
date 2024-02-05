package com.rettiwer.equipmentmanagement.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserItemsController {
    private final ItemService itemService;

    @GetMapping("/items")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(itemService.getAllUserItems(), HttpStatus.OK);
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<?> singleUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(itemService.getSingleUserItemsById(userId), HttpStatus.OK);
    }
}
