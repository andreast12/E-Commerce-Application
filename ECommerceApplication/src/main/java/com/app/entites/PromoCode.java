package com.app.entites;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "promo_codes")
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long promoCodeId;

	@NotBlank
	@Column(unique = true)
	private String code;

	@NotNull
	private Double discountPercentage;

	@NotNull
	private LocalDate expiryDate;

	private boolean active;

}
