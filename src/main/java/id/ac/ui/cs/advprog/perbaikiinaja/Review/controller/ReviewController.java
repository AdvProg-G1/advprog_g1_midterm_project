// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/controller/ReviewController.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.OverallRatingService;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OverallRatingService overallRatingService;


    @PostMapping
    public ResponseEntity<ReviewResponse> create(@RequestBody ReviewRequest req) {
        log.info("🔥 POST /api/reviews received: {}", req);
        ReviewResponse resp = reviewService.createReview(req);
        log.info("← POST /api/reviews created: {}", resp);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable String id,
            @RequestBody ReviewRequest req
    ) {
        return ResponseEntity.ok(reviewService.updateReview(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam String userId
    ) {
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<ReviewResponse>> byTechnician(
            @PathVariable String technicianId
    ) {
        return ResponseEntity.ok(reviewService.getReviewsForTechnician(technicianId));
    }

    @GetMapping("/best")
    public ResponseEntity<List<BestTechnicianResponse>> top(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "desc") String order
    ) {
        List<BestTechnicianResponse> list = overallRatingService.getTopTechnicians(limit);
        if ("asc".equalsIgnoreCase(order)) {
            Collections.reverse(list);
        }
        return ResponseEntity.ok(list);
    }
}