package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.model.Users;
import com.wryon.ecomproj1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody Users users){
//        return  new ResponseEntity<>(users, HttpStatus.CREATED);
//    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users users){
        return userService.registerUsers(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return userService.verify(users);
    }


}
