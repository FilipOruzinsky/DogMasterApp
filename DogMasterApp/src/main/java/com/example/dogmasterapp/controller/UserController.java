package com.example.dogmasterapp.controller;

import com.example.dogmasterapp.dto.UserDTO;
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

    @PostMapping("/me")
    public User UpdateCurrentUser(@RequestBody UserDTO userDTO) {
        return userService.updateCurrentUser(userDTO);
    }/*tu nieje lepsie mat ako parameter na returne  tiez UserDTO kedze na FE pri
    update nemame heslo tym padom tam neni cela entita

    ked vraciam teraz z BE updanutu entitu User videl by som v Devtools vserky
    premene ? kedze je tu na vstupe User a nie UserDTO

    mozno prerobit
    */
}


