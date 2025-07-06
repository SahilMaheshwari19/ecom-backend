package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.ProductDAO;
import com.wryon.ecomproj1.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getAllProduct() {
        return productDAO.findAll();
    }

    @Override
    public Product getProductByID(int id) {
        return productDAO.findById(id).orElse(null);
    }

    @Override
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        product.setAvailable(product.getQuantity() > 0);
        return productDAO.save(product);
    }

    @Override
    public Product UpdateProductById(int id, Product product, MultipartFile imageFile) throws IOException {
        if(productDAO.findById(id).isPresent()){
            if(imageFile != null && !imageFile.isEmpty()){
                product.setImageData(imageFile.getBytes());
                product.setImageName(imageFile.getOriginalFilename());
                product.setImageType(imageFile.getContentType());
            }
            return productDAO.save(product);
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteProductById(int id) {
        productDAO.deleteById(id);
    }
}
