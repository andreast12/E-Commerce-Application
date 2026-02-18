package com.app.entites;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long discountId;

	@NotBlank
	private String discountName;

	@Min(value = 1, message = "Discount percentage must be at least 1")
	@Max(value = 100, message = "Discount percentage must be at most 100")
	private double discountPercentage;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;
}
