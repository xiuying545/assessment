package com.assessment.models;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "book_title", nullable = false)
	private String bookTitle;


	@Column(name = "book_price", nullable = false)
	private BigDecimal bookPrice;


	@Column(name = "book_quantity", nullable = false)
	private Integer bookQuantity;
}