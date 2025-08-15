package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}

