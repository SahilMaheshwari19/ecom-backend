package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public void registerUsers(Users users){
        LOG.info("InSide registerUsers service Method -- UserService Class {}", users.toString());
        LOG.info("InSide registerUsers service Method -- Setting Password");
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        LOG.info("InSide registerUsers service Method -- Password Set, Making DAO Call to save");
        userRepo.save(users);
    }

    public String verify(Users users) {
        LOG.info("InSide verify service Method -- UserService Class {}", users.toString());
        LOG.info("InSide verify service Method -- Starting Authentication");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword())
        );
        if (authentication.isAuthenticated()) {
            LOG.info("InSide verify service isAuthenticated Method -- Authentication Successful");
            LOG.info("InSide verify service -- Creating Token");
            String token = jwtService.generateToken(users.getUsername());
            LOG.info("InSide verify service -- Token Created = {}", token);
            return token;
        }
        LOG.info("InSide verify service isAuthenticated Method -- Authentication Failed");
        return "fail";
    }

    public Users fetchUserDetails(String username) {
        LOG.info("InSide fetchUserDetails service Method {}", username);
        return userRepo.findByUsername(username);
    }
}
