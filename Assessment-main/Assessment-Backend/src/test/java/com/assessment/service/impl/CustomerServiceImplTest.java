package com.assessment.service.impl;

import com.assessment.exception.CustomerNotFoundException;
import com.assessment.models.Customer;
import com.assessment.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===== CREATE =====
    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");

        Customer saved = new Customer();
        saved.setId(1L);
        saved.setFirstName("John");

        when(customerRepository.save(customer)).thenReturn(saved);

        Customer result = customerService.create(customer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testCreateCustomerThrowsException() {
        Customer customer = new Customer();
        customer.setFirstName("John");

        when(customerRepository.save(customer)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.create(customer));
        assertEquals("DB error", ex.getMessage());
    }

    // ===== GET ALL =====
    @Test
    void testGetAllCustomers() {
        Customer c1 = new Customer();
        c1.setId(1L);
        Customer c2 = new Customer();
        c2.setId(2L);

        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Customer> customers = customerService.getAll();

        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCustomersThrowsException() {
        when(customerRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.getAll());
        assertEquals("DB error", ex.getMessage());
    }

    // ===== GET BY ID =====
    @Test
    void testGetByIdFound() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetCustomerThrowsException() {
        when(customerRepository.findById(any())).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.getById(any()));
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getById(1L));
    }

    // ===== UPDATE =====
    @Test
    void testUpdateCustomer() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setFirstName("Old");

        Customer updated = new Customer();
        updated.setFirstName("New");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        Customer result = customerService.update(1L, updated);

        assertEquals("New", result.getFirstName());
        verify(customerRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateCustomerThrowsExceptionOnSave() {
        Customer existing = new Customer();
        existing.setId(1L);

        Customer updated = new Customer();
        updated.setFirstName("New");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.update(1L, updated));
        assertEquals("DB error", ex.getMessage());
    }

    // ===== DELETE =====
    @Test
    void testDeleteCustomer() {
        Customer existing = new Customer();
        existing.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(customerRepository).delete(existing);

        customerService.delete(1L);

        verify(customerRepository, times(1)).delete(existing);
    }

    @Test
    void testDeleteCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.delete(1L));
    }

    @Test
    void testDeleteCustomerThrowsExceptionOnDelete() {
        Customer existing = new Customer();
        existing.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        doThrow(new RuntimeException("DB error")).when(customerRepository).delete(existing);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.delete(1L));
        assertEquals("DB error", ex.getMessage());
    }
}