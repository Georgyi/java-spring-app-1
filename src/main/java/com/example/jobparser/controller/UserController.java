package com.example.jobparser.controller;

import com.example.jobparser.dto.UserDto;
import com.example.jobparser.database.entity.User;
import com.example.jobparser.database.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with id: " + id + " is not found"
                ));
    }

    @GetMapping("/name/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userService
                .findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with username: " + username + " is not found"
                ));
    }

    @GetMapping("/test")
    public UserDto getUserById() {
        return UserDto.builder().email("ooshkap@gmail.com").build();
    }
}
