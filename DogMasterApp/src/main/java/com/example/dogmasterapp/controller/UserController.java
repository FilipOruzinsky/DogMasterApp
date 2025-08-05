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

    @GetMapping
    List<User> all() {
        return userService.getAllUsers();
    }

    @GetMapping("/email/{email}")
    User findByMail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/userId/{userId}")
    public User findByID(@PathVariable("userId") Integer userId) {
        return userService.getById(userId);
    }

    @PostMapping
    User user(@RequestBody User newUser) {
        return userService.saveUser(newUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    public User user(@PathVariable("userId") Integer userId, @RequestBody User patchedUser) {
        return userService.patchedUser(userId, patchedUser);
    }

    @PutMapping("/{userId}")
    public User userPut(@PathVariable("userId") Integer userId, @RequestBody User patchedUser) {
        return userService.updateUser(userId, patchedUser);
    }
}
