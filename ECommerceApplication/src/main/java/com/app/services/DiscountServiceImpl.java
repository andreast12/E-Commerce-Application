package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Discount;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.DiscountDTO;
import com.app.repositories.DiscountRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class DiscountServiceImpl implements DiscountService {

	@Autowired
	private DiscountRepo discountRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public DiscountDTO createDiscount(DiscountDTO discountDTO) {
		Discount discount = modelMapper.map(discountDTO, Discount.class);

		if (discount.getEndDate().isBefore(discount.getStartDate())) {
			throw new APIException("End date must be after start date");
		}

		Discount savedDiscount = discountRepo.save(discount);
		return modelMapper.map(savedDiscount, DiscountDTO.class);
	}

	@Override
	public List<DiscountDTO> getAllDiscounts() {
		List<Discount> discounts = discountRepo.findAll();

		if (discounts.isEmpty()) {
			throw new APIException("No store discounts found");
		}

		return discounts.stream()
				.map(d -> modelMapper.map(d, DiscountDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<DiscountDTO> getActiveDiscounts() {
		List<Discount> discounts = discountRepo.findActiveDiscounts(LocalDate.now());

		if (discounts.isEmpty()) {
			throw new APIException("No active store discounts at this time");
		}

		return discounts.stream()
				.map(d -> modelMapper.map(d, DiscountDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public DiscountDTO updateDiscount(Long discountId, DiscountDTO discountDTO) {
		Discount discount = discountRepo.findById(discountId)
				.orElseThrow(() -> new ResourceNotFoundException("Discount", "discountId", discountId));

		discount.setDiscountName(discountDTO.getDiscountName());
		discount.setDiscountPercentage(discountDTO.getDiscountPercentage());
		discount.setStartDate(discountDTO.getStartDate());
		discount.setEndDate(discountDTO.getEndDate());

		Discount updatedDiscount = discountRepo.save(discount);
		return modelMapper.map(updatedDiscount, DiscountDTO.class);
	}

	@Override
	public String deleteDiscount(Long discountId) {
		Discount discount = discountRepo.findById(discountId)
				.orElseThrow(() -> new ResourceNotFoundException("Discount", "discountId", discountId));

		discountRepo.delete(discount);
		return "Store discount '" + discount.getDiscountName() + "' deleted successfully";
	}
}
