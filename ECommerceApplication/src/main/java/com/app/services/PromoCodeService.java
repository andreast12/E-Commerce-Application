package com.app.services;

import java.util.List;

import com.app.entites.PromoCode;
import com.app.payloads.PromoCodeDTO;

public interface PromoCodeService {

	PromoCodeDTO createPromoCode(PromoCode promoCode);

	List<PromoCodeDTO> getAllPromoCodes();

	PromoCodeDTO getPromoCodeById(Long promoCodeId);

	PromoCodeDTO updatePromoCode(PromoCode promoCode, Long promoCodeId);

	String deletePromoCode(Long promoCodeId);

	PromoCodeDTO validatePromoCode(String code);

}
