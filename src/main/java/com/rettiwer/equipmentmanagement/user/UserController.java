package com.rettiwer.equipmentmanagement.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> single(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.replaceOrInsert(userDTO, null), HttpStatus.CREATED);
    }

    @PutMapping(value =  "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO userDTO, @Nullable @PathVariable("id") User user) {
        return new ResponseEntity<>(userService.replaceOrInsert(userDTO, user),
                user != null ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<?> delete(@Nullable @PathVariable("id") User user) {
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
