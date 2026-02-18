package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.DiscountDTO;
import com.app.services.DiscountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class DiscountController {

	@Autowired
	private DiscountService discountService;

	@PostMapping("/admin/discounts")
	public ResponseEntity<DiscountDTO> createDiscount(@Valid @RequestBody DiscountDTO discountDTO) {
		DiscountDTO createdDiscount = discountService.createDiscount(discountDTO);
		return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
	}

	@GetMapping("/admin/discounts")
	public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
		List<DiscountDTO> discounts = discountService.getAllDiscounts();
		return new ResponseEntity<>(discounts, HttpStatus.OK);
	}

	@GetMapping("/public/discounts/active")
	public ResponseEntity<List<DiscountDTO>> getActiveDiscounts() {
		List<DiscountDTO> discounts = discountService.getActiveDiscounts();
		return new ResponseEntity<>(discounts, HttpStatus.OK);
	}

	@PutMapping("/admin/discounts/{discountId}")
	public ResponseEntity<DiscountDTO> updateDiscount(@PathVariable Long discountId,
			@Valid @RequestBody DiscountDTO discountDTO) {
		DiscountDTO updatedDiscount = discountService.updateDiscount(discountId, discountDTO);
		return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
	}

	@DeleteMapping("/admin/discounts/{discountId}")
	public ResponseEntity<String> deleteDiscount(@PathVariable Long discountId) {
		String status = discountService.deleteDiscount(discountId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
}
