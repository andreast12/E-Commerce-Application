package com.app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Discount;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Long> {

	@Query("SELECT d FROM Discount d WHERE d.startDate <= ?1 AND d.endDate >= ?1")
	List<Discount> findActiveDiscounts(LocalDate currentDate);
}
