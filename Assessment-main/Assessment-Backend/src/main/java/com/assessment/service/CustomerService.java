package com.assessment.service;

import com.assessment.models.Customer;

import java.util.List;

public interface CustomerService {

	Customer create(Customer customer);

	List<Customer> getAll();

	Customer getById(Long id);

	Customer update(Long id, Customer customer);

	void delete(Long id);
}