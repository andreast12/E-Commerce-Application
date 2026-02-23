package com.app.services;

import com.app.entites.Review;
import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewResponse;

public interface ReviewService {

    ReviewDTO addReview(Long productId, Long userId, Review review);

    ReviewResponse getReviewsByProduct(Long productId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder);

    String deleteReview(Long reviewId);

}
