package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<Product> getAllProduct();
    Product getProductByID(int id);
    Product addProduct(Product product, MultipartFile imageFile) throws IOException;
    Product UpdateProductById(int id, Product product, MultipartFile imageFile) throws IOException;
    void deleteProductById(int id);
}
