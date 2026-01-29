package com.assessment.service.impl;

import com.assessment.exception.CustomerNotFoundException;
import com.assessment.models.Customer;
import com.assessment.repository.CustomerRepository;
import com.assessment.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


	private final CustomerRepository customerRepository;


	@Override
	public Customer create(Customer customer) {
		try {
			Optional<Customer> existing =
					customerRepository.findByEmail(customer.getPersonalEmail());

			if (existing.isPresent()) {
				log.info(
						"Customer already exists → id={}",
						existing.get().getId()
				);
				return existing.get();
			}

			Customer saved = customerRepository.save(customer);
			log.info("SUCCESS: Customer created → id={}", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("ERROR: Failed to create customer → name={} {}, error={}",
					customer.getFirstName(), customer.getLastName(), e.getMessage(), e);
			throw e;
		}
	}


	@Override
	public List<Customer> getAll() {
		log.info("Fetching all customers");
		try {
			List<Customer> customers = customerRepository.findAll();
			log.info("Fetched {} customers", customers.size());
			return customers;
		} catch (Exception e) {
			log.error("ERROR: Failed to fetch all customers → error={}", e.getMessage(), e);
			throw e;
		}
	}


	@Override
	public Customer getById(Long id) {
		try {
			Customer customer = customerRepository.findById(id)
					.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
			log.info("Customer fetched → id={}, name={} {}", customer.getId(), customer.getFirstName(), customer.getLastName());
			return customer;
		} catch (CustomerNotFoundException cnfe) {
			log.warn("WARN: {}", cnfe.getMessage());
			throw cnfe;
		} catch (Exception e) {
			log.error("ERROR: Failed to fetch customer by id={} → error={}", id, e.getMessage(), e);
			throw e;
		}
	}


	@Override
	public Customer update(Long id, Customer updated) {
		log.info("START: Updating customer → id={}", id);
		try {
			Customer existing = getById(id);
			existing.setFirstName(updated.getFirstName());
			existing.setLastName(updated.getLastName());
			existing.setOfficeEmail(updated.getOfficeEmail());
			existing.setPersonalEmail(updated.getPersonalEmail());
			existing.setFamilyMembers(updated.getFamilyMembers());


			Customer saved = customerRepository.save(existing);
			log.info("SUCCESS: Updated customer → id={}", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("ERROR: Failed to update customer → id={}, error={}", id, e.getMessage(), e);
			throw e;
		}
	}


	@Override
	public void delete(Long id) {
		log.info("START: Deleting customer → id={}", id);
		try {
			Customer existing = getById(id);
			customerRepository.delete(existing);
			log.info("SUCCESS: Deleted customer → id={}", id);
		} catch (CustomerNotFoundException cnfe) {
			log.warn("WARN: {}", cnfe.getMessage());
			throw cnfe;
		} catch (Exception e) {
			log.error("ERROR: Failed to delete customer → id={}, error={}", id, e.getMessage(), e);
			throw e;
		}
	}
}