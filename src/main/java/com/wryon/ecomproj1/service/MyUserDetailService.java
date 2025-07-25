package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.UserPrincipal;
import com.wryon.ecomproj1.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepo.findByUsername(username);
        if (users == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException(username + " Not Found");
        }
        return new UserPrincipal(users);
    }

    public boolean checkIfUserAlreadyExist(String username) {
        System.out.println("InSide checkIfUserAlreadyExist Method = " + username);
        Users users = userRepo.findByUsername(username);
        if (users == null) {
            System.out.println("User Not Found, Hence Unique username: " + username);
            return false;
        }
        System.out.println("checkIfUserAlreadyExist --  Username exists = " +  username);
        return true;
    }

}
