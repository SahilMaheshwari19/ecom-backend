package com.wryon.ecomproj1.DAO;

import com.wryon.ecomproj1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {


}
