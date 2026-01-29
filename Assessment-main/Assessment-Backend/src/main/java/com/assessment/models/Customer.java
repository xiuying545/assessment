package com.assessment.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.*;

@Entity
@Table(
		name = "customers",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "personal_email")
		}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "office_email")
	private String officeEmail;

	@Column(name = "personal_email")
	private String personalEmail;

	@Column(name = "family_members")
	private Integer familyMembers;
}