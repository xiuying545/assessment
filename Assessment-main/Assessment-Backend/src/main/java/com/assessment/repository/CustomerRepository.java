package com.assessment.repository;


import com.assessment.models.Customer;
import com.assessment.models.Product;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    Customer save(Customer customer);
    void delete(Customer customer);
}