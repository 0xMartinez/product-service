package com.example.cassandra.service;

import com.example.cassandra.exception.ResourceNotFoundException;
import com.example.cassandra.repository.ProductRepository;
import com.example.cassandra.repository.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
