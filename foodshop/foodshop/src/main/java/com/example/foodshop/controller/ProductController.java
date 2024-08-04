package com.example.foodshop.controller;

import com.example.foodshop.Entity.Product;
import com.example.foodshop.dto.ProductDto;
import com.example.foodshop.response.ResponseObject;
import com.example.foodshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllProducts() {
        try {
            var listproduct = productService.findAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Lấy danh sách sản phẩm thành công ",true, listproduct)
            );
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "Có lỗi xảy ra", false, "")
            );
        }

    }
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchProducts(@RequestParam String query) {
        try {
            var search = productService.searchProductsByName(query);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Search success",true, search)
            );
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred", false, "")
            );
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = new Product();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setStatus(productDto.getStatus());
            product.setImage(productDto.getImage());

            var savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(200, "Added product successfully",true, savedProduct)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred", false, "")
            );
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(200, "Edited the product successfully",true, updatedProduct)
            );
        }catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(403, "An error occurred" + ex.getMessage(), false, "")
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}