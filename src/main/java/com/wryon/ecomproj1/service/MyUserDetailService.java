package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.UserPrincipal;
import com.wryon.ecomproj1.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("InSide loadUserByUsername Method --  UserDetailService Class = {}", username);
        Users users = userRepo.findByUsername(username);
        if (users == null) {
            LOG.error("User Not Found");
            throw new UsernameNotFoundException(username + " Not Found");
        }
        return new UserPrincipal(users);
    }

    public boolean checkIfUserAlreadyExist(String username) {
        LOG.info("InSide checkIfUserAlreadyExist Method = {}", username);
        Users users = userRepo.findByUsername(username);
        if (users == null) {
            LOG.info("User Not Found, Hence Unique username: {}", username);
            return false;
        }
        LOG.info("Username exists = {}", username);
        return true;
    }
}
