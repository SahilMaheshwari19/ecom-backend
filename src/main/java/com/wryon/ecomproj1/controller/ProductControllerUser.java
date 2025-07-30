package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.model.Product;
import com.wryon.ecomproj1.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductControllerUser {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ProductService productService;

    @RequestMapping("/")
    public String greet(){
        LOG.info("InSide Greet Controller Method");
        return "Hello World";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct(){
        LOG.info("InSide getAllProduct Controller Method");
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable int id){
        LOG.info("InSide getProductByID Controller Method");
        Product product = productService.getProductByID(id);
        if(product != null){
            LOG.info("InSide getProductByID Controller Method -- Product fetched Successfully");
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            LOG.warn("InSide UpdateProductById Controller Method -- Product Not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getImageByProductID(@PathVariable int id){
        LOG.info("InSide getImageByProductID Controller Method");
        Product product = productService.getProductByID(id);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().body(imageFile);
    }
}
