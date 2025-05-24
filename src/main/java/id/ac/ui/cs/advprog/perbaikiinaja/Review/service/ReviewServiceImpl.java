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
    public ReviewResponse createReview(ReviewRequest request) {
        if (reviewRepository.findByUserIdAndTechnicianId(request.getUserId(), request.getTechnicianId()) != null) {
            throw new RuntimeException("Review already exists");
        }

        Review review = new Review();
        review.setTechnicianId(request.getTechnicianId());
        review.setUserId(request.getUserId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse getReviewById(String reviewId) {
        return reviewRepository.findById(reviewId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));

        if (!review.getUserId().equals(request.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(String reviewId, String userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
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
                .map(user -> user.getFullName())
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