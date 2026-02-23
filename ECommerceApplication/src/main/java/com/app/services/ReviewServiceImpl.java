package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Review;
import com.app.entites.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ReviewDTO;
import com.app.payloads.ReviewResponse;
import com.app.repositories.ProductRepo;
import com.app.repositories.ReviewRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReviewDTO addReview(Long productId, Long userId, Review review) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        review.setProduct(product);
        review.setUser(user);

        Review saved = reviewRepo.save(review);

        ReviewDTO dto = modelMapper.map(saved, ReviewDTO.class);
        dto.setProductId(product.getProductId());
        dto.setUserId(user.getUserId());
        dto.setUserEmail(user.getEmail());

        return dto;
    }

    @Override
    public ReviewResponse getReviewsByProduct(Long productId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Review> pageReviews = reviewRepo.findByProduct(product, pageable);

        List<ReviewDTO> dtos = pageReviews.getContent().stream().map(r -> {
            ReviewDTO d = modelMapper.map(r, ReviewDTO.class);
            d.setProductId(product.getProductId());
            if (r.getUser() != null) {
                d.setUserId(r.getUser().getUserId());
                d.setUserEmail(r.getUser().getEmail());
            }
            return d;
        }).collect(Collectors.toList());

        ReviewResponse response = new ReviewResponse();
        response.setContent(dtos);
        response.setPageNumber(pageReviews.getNumber());
        response.setPageSize(pageReviews.getSize());
        response.setTotalElements(pageReviews.getTotalElements());
        response.setTotalPages(pageReviews.getTotalPages());
        response.setLastPage(pageReviews.isLast());

        return response;
    }

    @Override
    public String deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", reviewId));

        reviewRepo.delete(review);

        return "Review with reviewId: " + reviewId + " deleted successfully !!!";
    }

}
