package com.wryon.ecomproj1.controller;

import com.wryon.ecomproj1.model.Product;
import com.wryon.ecomproj1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController{

    @Autowired
    private ProductService productService;

    @RequestMapping("/")
    public String greet(){
        return "Hello World";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable int id){
        Product product = productService.getProductByID(id);
        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try {
            Product newProduct = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getImageByProductID(@PathVariable int id){
        Product product = productService.getProductByID(id);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> UpdateProductById(@PathVariable int id, @RequestPart Product product, @RequestPart(required = false) MultipartFile imageFile ) throws IOException {
        Product updatedProduct = productService.UpdateProductById(id, product, imageFile);
        if(updatedProduct != null){
            return new ResponseEntity<>("Product Updated Successfully", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not Updated Successfully", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        try {
            productService.deleteProductById(id);
            return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( e.getMessage() + " -- Product Not Deleted Successfully", HttpStatus.NOT_FOUND);
        }
    }
}
