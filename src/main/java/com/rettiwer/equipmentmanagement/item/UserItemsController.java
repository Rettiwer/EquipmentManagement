package com.rettiwer.equipmentmanagement.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(itemService.getAllUserItemsById(userId), HttpStatus.OK);
    }
}
