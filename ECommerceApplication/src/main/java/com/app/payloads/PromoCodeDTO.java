package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO {
	private Long promoCodeId;
	private String code;
	private Double discountPercentage;
	private LocalDate expiryDate;
	private boolean active;
}
