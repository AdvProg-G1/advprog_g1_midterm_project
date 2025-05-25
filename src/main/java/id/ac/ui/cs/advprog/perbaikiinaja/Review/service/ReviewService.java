//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/service/ReviewService.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewRequest reviewRequest);
    ReviewResponse getReviewById(String reviewId);
    ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest);
    void deleteReview(String reviewId, String userId);
    List<ReviewResponse> getReviewsForTechnician(String technicianId);
}