package com.app.services;

import java.util.List;

import com.app.payloads.DiscountDTO;

public interface DiscountService {

	DiscountDTO createDiscount(DiscountDTO discountDTO);

	List<DiscountDTO> getAllDiscounts();

	List<DiscountDTO> getActiveDiscounts();

	DiscountDTO updateDiscount(Long discountId, DiscountDTO discountDTO);

	String deleteDiscount(Long discountId);
}
