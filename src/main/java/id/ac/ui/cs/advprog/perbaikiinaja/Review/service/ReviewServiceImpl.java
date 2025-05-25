// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/service/ReviewServiceImpl.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        // prevent duplicate
        Review existing = reviewRepository.findByUserIdAndTechnicianId(
                reviewRequest.getUserId(), reviewRequest.getTechnicianId());
        if (existing != null) {
            throw new RuntimeException("Review already exists");
        }

        Review r = new Review();
        r.setTechnicianId(reviewRequest.getTechnicianId());
        r.setUserId(reviewRequest.getUserId());
        r.setRating(reviewRequest.getRating());
        r.setComment(reviewRequest.getComment());
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());

        r = reviewRepository.save(r);
        return mapToResponse(r);
    }

    @Override
    public ReviewResponse getReviewById(String reviewId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));
        return mapToResponse(r);
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));
        if (!r.getUserId().equals(reviewRequest.getUserId())) {
            throw new RuntimeException("Unauthorized to update review");
        }
        r.setRating(reviewRequest.getRating());
        r.setComment(reviewRequest.getComment());
        r.setUpdatedAt(LocalDateTime.now());
        r = reviewRepository.save(r);
        return mapToResponse(r);
    }

    @Override
    public void deleteReview(String reviewId, String userId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!r.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete review");
        }
        reviewRepository.delete(r);
    }

    @Override
    public List<ReviewResponse> getReviewsForTechnician(String technicianId) {
        return reviewRepository.findByTechnicianId(technicianId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        String reviewerName = userRepository.findById(review.getUserId())
                .map(u -> u.getFullName())
                .orElse(review.getUserId());
        return new ReviewResponse(
                review.getId(),
                review.getTechnicianId(),
                review.getUserId(),
                review.getRating(),
                review.getComment(),
                reviewerName
        );
    }
}