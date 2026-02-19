package org.tibocv.apicv.controller;

import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.model.Users;
import org.tibocv.apicv.service.UserService;

@RequestMapping("/api/auth")
@RestController
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return userService.verify(user);
    }

}
