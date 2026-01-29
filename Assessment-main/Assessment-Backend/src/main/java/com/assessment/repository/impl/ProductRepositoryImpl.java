package com.assessment.repository.impl;

import com.assessment.models.Product;
import com.assessment.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Product> findById(Long id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }


    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }


    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
            return product;
        } else {
            return entityManager.merge(product);
        }
    }

    @Override
    public void delete(Product product) {
        if (!entityManager.contains(product)) {
            product = entityManager.merge(product);
        }
        entityManager.remove(product);
    }

    @Override
    public Optional<Product> findByBookTitle(String bookTitle) {
        return entityManager
                .createQuery(
                        "SELECT p FROM Product p WHERE p.bookTitle = :bookTitle",
                        Product.class
                )
                .setParameter("bookTitle", bookTitle)
                .getResultStream()
                .findFirst();
    }
}