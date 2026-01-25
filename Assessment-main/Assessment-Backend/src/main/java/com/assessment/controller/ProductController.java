package com.assessment.controller;
import com.assessment.models.Product;
import com.assessment.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API")
public class ProductController {


	private final ProductService productService;


	@PostMapping
	public Product create(@RequestBody @NotNull(message = "Product cannot be null") Product product) {
		Assert.hasText(product.getBookTitle(), "Book title must not be empty");
		Assert.notNull(product.getBookPrice(), "Book price must not be null");
		Assert.notNull(product.getBookQuantity(), "Book quantity must not be null");
		return productService.create(product);
	}


	@GetMapping
	public List<Product> getAll() {
		return productService.getAll();
	}


	@GetMapping("/{id}")
	public Product getById(@PathVariable @NotNull(message = "Product ID cannot be null") Long id) {
		return productService.getById(id);
	}


	@PatchMapping("/{id}")
	public Product update(@PathVariable @NotNull(message = "Product ID cannot be null") Long id, @RequestBody @NotNull(message = "Product cannot be null")Product product) {
		return productService.update(id, product);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable @NotNull(message = "Product ID cannot be null") Long id) {
		productService.delete(id);
	}
}