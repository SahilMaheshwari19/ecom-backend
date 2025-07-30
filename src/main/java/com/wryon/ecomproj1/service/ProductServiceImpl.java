package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.ProductDAO;
import com.wryon.ecomproj1.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getAllProduct() {
        LOG.info("Inside getAllProduct method -- ProductServiceImpl class");
        LOG.info("Inside getAllProduct method -- Making DAO Call");
        return productDAO.findAll();
    }

    @Override
    public Product getProductByID(int id) {
        LOG.info("Inside getProductByID method -- ProductServiceImpl class {}", id);
        LOG.info("Inside getProductByID method -- Making DAO Call {}", id);
        return productDAO.findById(id).orElse(null);
    }

    @Override
    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        LOG.info("Inside addProduct method -- ProductServiceImpl class {}", product);
        LOG.info("Inside addProduct method -- Setting Image Details");
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        product.setAvailable(product.getQuantity() > 0);
        LOG.info("Inside addProduct method -- Making DAO Call {}", product);
        return productDAO.save(product);
    }

    @Override
    public Product UpdateProductById(int id, Product product, MultipartFile imageFile) throws IOException {
        LOG.info("Inside UpdateProductById method -- ProductServiceImpl class {}", product);
        if (productDAO.findById(id).isPresent()) {
            LOG.info("Inside UpdateProductById id IF BLOCK -- Product Exists");
            if (imageFile != null && !imageFile.isEmpty()) {
                LOG.info("Inside UpdateProductById Image IF BLOCK - admin uploaded an Image");
                product.setImageData(imageFile.getBytes());
                product.setImageName(imageFile.getOriginalFilename());
                product.setImageType(imageFile.getContentType());
            }
            LOG.info("Inside UpdateProductById method - Update Successful - Making DAO Call {}", product);
            return productDAO.save(product);
        } else {
            LOG.info("Inside UpdateProductById method - No Product Exists with id {} ", id);
            return null;
        }
    }

    @Override
    public void deleteProductById(int id) {
        LOG.info("Inside deleteProductById method -- ProductServiceImpl class {}", id);
        LOG.info("Inside deleteProductById method - Delete Successful - Making DAO Call {}", id);
        productDAO.deleteById(id);
    }
}
