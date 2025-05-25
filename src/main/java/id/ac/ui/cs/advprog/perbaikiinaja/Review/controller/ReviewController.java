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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ReviewResponse> create(@RequestBody ReviewRequest req) {
        log.debug("🔥 POST /api/reviews » payload={}", req);
        ReviewResponse resp = reviewService.createReview(req);
        log.info("✅ Review created id={} for technician={}", resp.getId(), resp.getTechnicianId());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'CUSTOMER')")
    public ResponseEntity<ReviewResponse> getOne(@PathVariable String id) {
        log.debug("→ GET /api/reviews/{}", id);
        ReviewResponse resp = reviewService.getReviewById(id);
        log.info("✅ Fetched review id={}", id);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable String id,
            @RequestBody ReviewRequest req
    ) {
        log.debug("🔥 PUT /api/reviews/{} » payload={}", id, req);
        ReviewResponse resp = reviewService.updateReview(id, req);
        log.info("✅ Review updated id={}", id);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam String userId
    ) {
        log.debug("🔥 DELETE /api/reviews/{}?userId={}", id, userId);
        reviewService.deleteReview(id, userId);
        log.info("✅ Review deleted id={} by user={}", id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<ReviewResponse>> byTechnician(
            @PathVariable String technicianId
    ) {
        log.debug("→ GET /api/reviews/technician/{}", technicianId);
        List<ReviewResponse> list = reviewService.getReviewsForTechnician(technicianId);
        log.info("✅ Returned {} reviews for technician={}", list.size(), technicianId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/best")
    public ResponseEntity<List<BestTechnicianResponse>> top(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "desc") String order
    ) {
        log.debug("→ GET /api/reviews/best?limit={}&order={}", limit, order);
        List<BestTechnicianResponse> list = overallRatingService.getTopTechnicians(limit);
        if ("asc".equalsIgnoreCase(order)) {
            Collections.reverse(list);
        }
        log.info("✅ Leaderboard generated size={} order={}", list.size(), order);
        return ResponseEntity.ok(list);
    }
}