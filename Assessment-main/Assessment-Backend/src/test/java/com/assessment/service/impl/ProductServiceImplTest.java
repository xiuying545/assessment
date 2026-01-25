package com.assessment.service.impl;

import com.assessment.exception.ProductNotFoundException;
import com.assessment.kafka.ProductEventProducer;
import com.assessment.models.Product;
import com.assessment.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEventProducer productEventProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setBookTitle("Java");
        product.setBookPrice(BigDecimal.valueOf(50.0));
        product.setBookQuantity(10);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setBookTitle("Java");

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.create(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(product);
        verify(productEventProducer, times(1)).sendProductCreatedEvent(savedProduct);
    }

    @Test
    void testGetAllProducts() {
        Product p1 = new Product();
        p1.setId(1L);
        Product p2 = new Product();
        p2.setId(2L);

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> products = productService.getAll();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdFound() {
        Product product = new Product();
        product.setId(1L);
        product.setBookTitle("Java");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getById(1L);

        assertNotNull(result);
        assertEquals("Java", result.getBookTitle());
    }

    @Test
    void testGetByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void testUpdateProduct() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setBookTitle("Old");

        Product updated = new Product();
        updated.setBookTitle("New");
        updated.setBookPrice(BigDecimal.valueOf(100.0));
        updated.setBookQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);

        Product result = productService.update(1L, updated);

        assertEquals("New", result.getBookTitle());
        assertEquals(BigDecimal.valueOf(100.0), result.getBookPrice());
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    void testDeleteProduct() {
        Product existing = new Product();
        existing.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(productRepository).delete(existing);

        productService.delete(1L);

        verify(productRepository, times(1)).delete(existing);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));
    }

    @Test
    void testCreateProductThrowsException() {
        Product product = new Product();
        product.setBookTitle("Java");

        // Simulate repository throwing an exception
        when(productRepository.save(product)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.create(product));
        assertEquals("DB error", ex.getMessage());

        // Verify that Kafka event was never called
        verify(productEventProducer, never()).sendProductCreatedEvent(any());
    }

    @Test
    void testGetAllProductsThrowsException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.getAll());
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetProductThrowsException() {
        when(productRepository.findById(any())).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.getById(any()));
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testUpdateProductThrowsExceptionOnSave() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setBookTitle("Old");

        Product updated = new Product();
        updated.setBookTitle("New");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.update(1L, updated));
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testDeleteProductThrowsExceptionOnDelete() {
        Product existing = new Product();
        existing.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        doThrow(new RuntimeException("DB error")).when(productRepository).delete(existing);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.delete(1L));
        assertEquals("DB error", ex.getMessage());
    }
}