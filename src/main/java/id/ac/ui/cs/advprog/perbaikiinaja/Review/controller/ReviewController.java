//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/controller/ReviewController.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable("id") String reviewId,
                                                       @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") String reviewId,
                                             @RequestParam("userId") String userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable("technicianId") String technicianId) {
        List<ReviewResponse> responses = reviewService.getReviewsForTechnician(technicianId);
        return ResponseEntity.ok(responses);
    }
}