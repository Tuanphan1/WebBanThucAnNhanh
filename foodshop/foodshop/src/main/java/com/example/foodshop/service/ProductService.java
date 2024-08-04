package com.example.foodshop.service;

import com.example.foodshop.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Optional<Product> findProductById(Long id);
    Product saveProduct(Product product);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    List<Product> searchProductsByName(String name);
}
