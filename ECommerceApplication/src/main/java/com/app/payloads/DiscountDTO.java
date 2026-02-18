package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

	private Long discountId;
	private String discountName;
	private double discountPercentage;
	private LocalDate startDate;
	private LocalDate endDate;
}
