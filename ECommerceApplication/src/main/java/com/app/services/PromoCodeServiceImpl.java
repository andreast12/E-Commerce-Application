package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.PromoCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.PromoCodeDTO;
import com.app.repositories.PromoCodeRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class PromoCodeServiceImpl implements PromoCodeService {

	@Autowired
	private PromoCodeRepo promoCodeRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PromoCodeDTO createPromoCode(PromoCode promoCode) {
		PromoCode existing = promoCodeRepo.findByCode(promoCode.getCode());

		if (existing != null) {
			throw new APIException("Promo code '" + promoCode.getCode() + "' already exists !!!");
		}

		PromoCode saved = promoCodeRepo.save(promoCode);

		return modelMapper.map(saved, PromoCodeDTO.class);
	}

	@Override
	public List<PromoCodeDTO> getAllPromoCodes() {
		List<PromoCode> promoCodes = promoCodeRepo.findAll();

		if (promoCodes.size() == 0) {
			throw new APIException("No promo codes found");
		}

		return promoCodes.stream()
				.map(promoCode -> modelMapper.map(promoCode, PromoCodeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public PromoCodeDTO getPromoCodeById(Long promoCodeId) {
		PromoCode promoCode = promoCodeRepo.findById(promoCodeId)
				.orElseThrow(() -> new ResourceNotFoundException("PromoCode", "promoCodeId", promoCodeId));

		return modelMapper.map(promoCode, PromoCodeDTO.class);
	}

	@Override
	public PromoCodeDTO updatePromoCode(PromoCode promoCode, Long promoCodeId) {
		promoCodeRepo.findById(promoCodeId)
				.orElseThrow(() -> new ResourceNotFoundException("PromoCode", "promoCodeId", promoCodeId));

		promoCode.setPromoCodeId(promoCodeId);

		PromoCode saved = promoCodeRepo.save(promoCode);

		return modelMapper.map(saved, PromoCodeDTO.class);
	}

	@Override
	public String deletePromoCode(Long promoCodeId) {
		PromoCode promoCode = promoCodeRepo.findById(promoCodeId)
				.orElseThrow(() -> new ResourceNotFoundException("PromoCode", "promoCodeId", promoCodeId));

		promoCodeRepo.delete(promoCode);

		return "Promo code with promoCodeId: " + promoCodeId + " deleted successfully !!!";
	}

	@Override
	public PromoCodeDTO validatePromoCode(String code) {
		PromoCode promoCode = promoCodeRepo.findByCode(code);

		if (promoCode == null) {
			throw new ResourceNotFoundException("PromoCode", "code", code);
		}

		if (!promoCode.isActive()) {
			throw new APIException("Promo code '" + code + "' is not active");
		}

		if (promoCode.getExpiryDate().isBefore(LocalDate.now())) {
			throw new APIException("Promo code '" + code + "' has expired");
		}

		return modelMapper.map(promoCode, PromoCodeDTO.class);
	}

}
