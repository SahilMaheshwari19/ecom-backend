package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.Users;
import com.wryon.ecomproj1.service.JWTService;
import com.wryon.ecomproj1.service.MyUserDetailService;
import com.wryon.ecomproj1.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailService myUserDetailService;

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users, HttpServletResponse response) {
        LOG.info("InSide Login Controller Method = {}", users.toString());
        String jwtToken = userService.verify(users);
        if (!jwtToken.equals("fail")) {
            LOG.info("jwtToken success -- creating cookie");
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 7);    // 7 Days
            cookie.setPath("/");            // Access to whole App
            // cookie.setSecure(true); // enable in production
            // cookie.setSameSite("Strict"); // adjust for CSRF protection
            LOG.info("jwtToken success -- sending response cookie");
            response.addCookie(cookie);

            //fetch username and role
            LOG.info("fetch username and role to pass role in jwt");
            Users user = userService.fetchUserDetails(users.getUsername());

            Map<String, Object> responseBody = new HashMap<>();
            LOG.info("Username Fetched Successfully ,adding role in response body");
            responseBody.put("message", "Login Success");
            responseBody.put("username", user.getUsername());
            responseBody.put("role", user.getRole());
            LOG.info("Login Controller Method Map response{}", responseBody.toString());
            return ResponseEntity.ok(responseBody);
        } else {
            LOG.error("jwtToken fail -- Invalid Creds");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed -> Invalid Creds");
        }
    }

    @PostMapping("/logsout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        LOG.info("InSide logout Controller Method --- Deleting cookie");
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users users) {
        LOG.info("InSide Register Controller Method = {}", users.toString());
        if (myUserDetailService.checkIfUserAlreadyExist(users.getUsername())) {
            LOG.info("InSide Register Controller Method 1st if block = {}", users.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username Already Exist");
        } else {
            LOG.info("InSide Register Controller Method else block = {}", users.toString());
            users.setRole("ROLE_USER");
            userService.registerUsers(users);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registered Successfully");
        }
    }
}
