package com.example.foodshop.service.IMPL;

import com.example.foodshop.Entity.Product;
import com.example.foodshop.repository.ProductRepository;
import com.example.foodshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found on :: " + id));
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setDescription(productDetails.getDescription());
        product.setImage(productDetails.getImage());
        product.setStatus(productDetails.getStatus());
        return repository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found on :: " + id));
        repository.delete(product);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return repository.findByNameContaining(name);
    }
}
