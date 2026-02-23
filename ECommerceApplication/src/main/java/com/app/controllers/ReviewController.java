package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.entites.Review;
import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewResponse;
import com.app.services.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/user/products/{productId}/reviews")
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long productId,
            @RequestParam(name = "userId", required = true) Long userId, @Valid @RequestBody Review review) {

        ReviewDTO saved = reviewService.addReview(productId, userId, review);

        return new ResponseEntity<ReviewDTO>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/public/products/{productId}/reviews")
    public ResponseEntity<ReviewResponse> getReviewsByProduct(@PathVariable Long productId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ReviewResponse response = reviewService.getReviewsByProduct(productId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ReviewResponse>(response, HttpStatus.FOUND);
    }

}
