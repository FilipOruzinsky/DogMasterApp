package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import com.example.dogmasterapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private UserService userService;

    @GetMapping("/me")
        public User getCurrentUser(){
            return userService.getCurrentUser();
        }
    }

