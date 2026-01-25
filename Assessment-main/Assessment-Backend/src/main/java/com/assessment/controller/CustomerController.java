package com.assessment.controller;

import com.assessment.models.Customer;
import com.assessment.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer API")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping
	public Customer create(@RequestBody @NotNull(message = "Customer cannot be null") Customer customer) {
		Assert.hasText(customer.getFirstName(), "First Name must not be empty");
		Assert.hasText(customer.getLastName(), "Last Name must not be empty");
		Assert.hasText(customer.getPersonalEmail(), "Email must not be empty");
		return customerService.create(customer);
	}

	@GetMapping
	public List<Customer> getAll() {
		return customerService.getAll();
	}

	@GetMapping("/{id}")
	public Customer getById(@PathVariable @NotNull(message = "Customer Id cannot be null") Long id) {
		return customerService.getById(id);
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @RequestBody @NotNull(message = "Customer Id cannot be null") Customer customer) {
		return customerService.update(id, customer);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable @NotNull(message = "Product ID cannot be null") Long id) {
		customerService.delete(id);
	}
}