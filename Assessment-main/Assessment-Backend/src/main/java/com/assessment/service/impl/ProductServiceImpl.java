package com.assessment.service.impl;

import java.util.List;

import com.assessment.exception.ProductNotFoundException;
import com.assessment.kafka.ProductEventProducer;import com.assessment.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;import org.springframework.stereotype.Service;
import com.assessment.repository.ProductRepository;
import com.assessment.models.Product;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductEventProducer productEventProducer;

	@Override
	public Product create(Product product) {
		try {
			Product saved = productRepository.save(product);
			log.info("SUCCESS: Product created → id={}", saved.getId());

			log.info("Sending Kafka event for productId={}", saved.getId());
			productEventProducer.sendProductCreatedEvent(saved);
			return saved;
		} catch (Exception e) {
			log.error("ERROR: Failed to create product → title={}, error={}", product.getBookTitle(), e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public List<Product> getAll() {
		log.info("Fetching all products from DB");
		try {
			List<Product> products = productRepository.findAll();
			log.info("Fetched {} products", products.size());
			return products;
		} catch (Exception e) {
			log.error("ERROR: Failed to fetch all products → error={}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Cacheable(value = "products", key = "#id")
	public Product getById(Long id) {
		try {
			Product product = productRepository.findById(id)
					.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
			log.info("Product fetched → id={}, title={}", product.getId(), product.getBookTitle());
			return product;
		} catch (ProductNotFoundException pnfe) {
			log.warn("WARN: {}", pnfe.getMessage());
			throw pnfe;
		} catch (Exception e) {
			log.error("ERROR: Failed to fetch product by id={} → error={}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@CacheEvict(value = "productList", allEntries = true)
	public Product update(Long id, Product updated) {
		log.info("START: Updating product → id={}", id);
		try {
			Product existing = getById(id);
			existing.setBookTitle(updated.getBookTitle());
			existing.setBookPrice(updated.getBookPrice());
			existing.setBookQuantity(updated.getBookQuantity());

			Product saved = productRepository.save(existing);
			log.info("SUCCESS: Updated product → id={}", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("ERROR: Failed to update product → id={}, error={}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@CacheEvict(value = "products", key = "#id")
	public void delete(Long id) {
		log.info("START: Deleting product → id={}", id);
		try {
			Product existing = getById(id);
			productRepository.delete(existing);
			log.info("SUCCESS: Deleted product → id={}", id);
		} catch (ProductNotFoundException pnfe) {
			log.warn("WARN: {}", pnfe.getMessage());
			throw pnfe;
		} catch (Exception e) {
			log.error("ERROR: Failed to delete product → id={}, error={}", id, e.getMessage(), e);
			throw e;
		}
	}
}