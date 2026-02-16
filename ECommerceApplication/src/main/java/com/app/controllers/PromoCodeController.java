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

import com.app.entites.PromoCode;
import com.app.payloads.PromoCodeDTO;
import com.app.services.PromoCodeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class PromoCodeController {

	@Autowired
	private PromoCodeService promoCodeService;

	@PostMapping("/admin/promo-codes")
	public ResponseEntity<PromoCodeDTO> createPromoCode(@Valid @RequestBody PromoCode promoCode) {
		PromoCodeDTO savedPromoCodeDTO = promoCodeService.createPromoCode(promoCode);

		return new ResponseEntity<PromoCodeDTO>(savedPromoCodeDTO, HttpStatus.CREATED);
	}

	@GetMapping("/admin/promo-codes")
	public ResponseEntity<List<PromoCodeDTO>> getAllPromoCodes() {
		List<PromoCodeDTO> promoCodeDTOs = promoCodeService.getAllPromoCodes();

		return new ResponseEntity<List<PromoCodeDTO>>(promoCodeDTOs, HttpStatus.OK);
	}

	@GetMapping("/admin/promo-codes/{promoCodeId}")
	public ResponseEntity<PromoCodeDTO> getPromoCodeById(@PathVariable Long promoCodeId) {
		PromoCodeDTO promoCodeDTO = promoCodeService.getPromoCodeById(promoCodeId);

		return new ResponseEntity<PromoCodeDTO>(promoCodeDTO, HttpStatus.OK);
	}

	@PutMapping("/admin/promo-codes/{promoCodeId}")
	public ResponseEntity<PromoCodeDTO> updatePromoCode(@RequestBody PromoCode promoCode, @PathVariable Long promoCodeId) {
		PromoCodeDTO promoCodeDTO = promoCodeService.updatePromoCode(promoCode, promoCodeId);

		return new ResponseEntity<PromoCodeDTO>(promoCodeDTO, HttpStatus.OK);
	}

	@DeleteMapping("/admin/promo-codes/{promoCodeId}")
	public ResponseEntity<String> deletePromoCode(@PathVariable Long promoCodeId) {
		String status = promoCodeService.deletePromoCode(promoCodeId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

	@GetMapping("/public/promo-codes/validate/{code}")
	public ResponseEntity<PromoCodeDTO> validatePromoCode(@PathVariable String code) {
		PromoCodeDTO promoCodeDTO = promoCodeService.validatePromoCode(code);

		return new ResponseEntity<PromoCodeDTO>(promoCodeDTO, HttpStatus.OK);
	}

}
