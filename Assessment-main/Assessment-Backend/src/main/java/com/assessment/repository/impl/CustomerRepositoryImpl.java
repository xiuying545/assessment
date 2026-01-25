package com.assessment.repository.impl;

import com.assessment.models.Customer;
import com.assessment.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Customer> findById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        return Optional.ofNullable(customer);
    }


    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }


    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            entityManager.persist(customer);
            return customer;
        } else {
            return entityManager.merge(customer);
        }
    }

    @Override
    public void delete(Customer customer) {
        if (!entityManager.contains(customer)) {
            customer = entityManager.merge(customer);
        }
        entityManager.remove(customer);
    }
}