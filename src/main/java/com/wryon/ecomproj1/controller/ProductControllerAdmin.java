package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.model.Product;
import com.wryon.ecomproj1.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class ProductControllerAdmin {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        LOG.info("InSide addProduct Controller Method");
        try {
            LOG.info("InSide addProduct Controller -- TRY BLOCK");
            Product newProduct = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error("InSide addProduct Controller -- CATCH BLOCK {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> UpdateProductById(@PathVariable int id, @RequestPart Product product, @RequestPart(required = false) MultipartFile imageFile ) throws IOException {
        LOG.info("InSide UpdateProductById Controller Method");
        Product updatedProduct = productService.UpdateProductById(id, product, imageFile);
        if(updatedProduct != null){
            LOG.info("InSide UpdateProductById Controller Method -- Product Updated Successfully");
            return new ResponseEntity<>("Product Updated Successfully", HttpStatus.OK);
        }
        else{
            LOG.warn("InSide UpdateProductById Controller Method -- Product Not Updated");
            return new ResponseEntity<>("Product Not Updated Successfully", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        LOG.info("InSide deleteProductById Controller Method");
        try {
            LOG.info("InSide deleteProductById Controller -- TRY BLOCK");
            productService.deleteProductById(id);
            LOG.info("InSide deleteProductById Controller -- TRY BLOCK -- Product Deleted Successfully");
            return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("InSide deleteProductById Controller -- CATCH BLOCK {}", e.getMessage());
            return new ResponseEntity<>( e.getMessage() + " -- Product Not Deleted Successfully", HttpStatus.NOT_FOUND);
        }
    }
}
