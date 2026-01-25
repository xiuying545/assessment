package com.assessment.service;

import java.util.List;

import com.assessment.models.Product;

public interface ProductService {

	Product create(Product product);

	List<Product> getAll();

	Product getById(Long id);

	Product update(Long id, Product product);

	void delete(Long id);
}