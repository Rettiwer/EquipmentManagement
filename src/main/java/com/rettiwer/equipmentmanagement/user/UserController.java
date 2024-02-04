package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(userService.create(registerRequest), HttpStatus.CREATED);
    }

    @PutMapping(value =  "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO userDTO, @PathVariable("id") Integer userId) {
        return new ResponseEntity<>(userService.replace(userDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
