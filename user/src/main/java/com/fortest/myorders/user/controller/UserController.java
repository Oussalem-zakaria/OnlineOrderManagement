package com.fortest.myorders.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fortest.myorders.user.bean.User;
import com.fortest.myorders.user.request.UserRequest;
import com.fortest.myorders.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
public record UserController(UserService userService) {

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest) {
        log.info("new user registration {}", userRequest);
        User newUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);  // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
