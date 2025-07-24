package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.model.Users;
import com.wryon.ecomproj1.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> login(@RequestBody Users users, HttpServletResponse response){
        System.out.println("InSide Login Controller Method = " + users.toString());
            String jwtToken = userService.verify(users);
        if(!jwtToken.equals("fail")){
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60*60*24*7);    // 7 Days
            cookie.setPath("/");            // Access to whole App
            // cookie.setSecure(true); // enable in production
            // cookie.setSameSite("Strict"); // adjust for CSRF protection
            response.addCookie(cookie);
            return ResponseEntity.ok("Login Success");
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed -> Invalid Creds");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        System.out.println("InSide logout Controller Method = ");

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");


    }
}
