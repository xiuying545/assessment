package com.assessment.repository;

import com.assessment.models.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Product save(Product product);
    void delete(Product product);
}