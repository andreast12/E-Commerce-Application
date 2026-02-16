package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.PromoCode;

@Repository
public interface PromoCodeRepo extends JpaRepository<PromoCode, Long> {

	PromoCode findByCode(String code);

}
