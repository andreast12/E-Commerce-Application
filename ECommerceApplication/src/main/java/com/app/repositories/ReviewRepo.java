package com.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Product;
import com.app.entites.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    Page<Review> findByProduct(Product product, Pageable pageable);

}
