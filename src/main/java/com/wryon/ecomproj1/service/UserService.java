package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public void registerUsers(Users users){
        System.out.println("InSide registerUsers service Method " + users.toString());
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        userRepo.save(users);
    }

    public String verify(Users users) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword())
        );
        System.out.println("Authentication = " + authentication);
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(users.getUsername());
            System.out.println("token = " + token);
            return token;
        }
        return "fail";
    }

    public Users fetchUserDetails(String username) {
        System.out.println("InSide fetchUserDetails service Method " + username);
        return userRepo.findByUsername(username);
    }
}
