package com.wryon.ecomproj1.DAO;

import com.wryon.ecomproj1.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Integer> {

    Users findByUsername(String username);
}