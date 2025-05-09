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
    private final UserRepository userRepository;    // ← Injected

    @Override
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        Review existing = reviewRepository.findByUserIdAndTechnicianId(
                reviewRequest.getUserId(), reviewRequest.getTechnicianId());
        if (existing != null) {
            throw new RuntimeException("Review already exists");
        }
        Review review = new Review();
        review.setTechnicianId(reviewRequest.getTechnicianId());
        review.setUserId(reviewRequest.getUserId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        review = reviewRepository.save(review);
        return mapToResponse(review);
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found for id: " + reviewId));
        if (!review.getUserId().equals(reviewRequest.getUserId())) {
            throw new RuntimeException("Unauthorized to update review for review id: " + reviewId);
        }
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUpdatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);
        return mapToResponse(review);
    }

    @Override
    public void deleteReview(String reviewId, String userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete review");
        }
        reviewRepository.delete(review);
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
                reviewerName                  // ← Populate full name
        );
    }
}