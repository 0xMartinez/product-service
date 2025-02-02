package com.example.cassandra.repository;

import com.example.cassandra.repository.model.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CassandraRepository<Product, UUID> {
    List<Product> findByCategory(String category);
}