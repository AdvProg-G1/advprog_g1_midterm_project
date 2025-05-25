// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/service/OverallRatingService.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy.RatingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OverallRatingService {

    private final ReviewRepository reviewRepository;
    private final RatingStrategy   ratingStrategy;
    private final UserRepository   userRepository;

    /* ---------- Single technician ---------- */

    public double calculateOverallRating(String techId) {
        List<Review> reviews = reviewRepository.findByTechnicianId(techId);
        return ratingStrategy.calculateRating(reviews);
    }

    /* ---------- Leaderboard ---------- */

    public List<BestTechnicianResponse> getTopTechnicians(int limit) {

        /* 1.  Group all reviews by technician once (O(N)) */
        Map<String, List<Review>> byTech =
                reviewRepository.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(Review::getTechnicianId));

        /* 2.  Build DTOs, skip tech IDs that aren’t in users table */
        return byTech.entrySet()
                .stream()
                .map(entry -> toBestTech(entry.getKey(), entry.getValue()))
                .filter(dto -> !"Unknown Technician".equals(dto.getFullName()))
                .sorted(
                        Comparator.comparingDouble(BestTechnicianResponse::getAverageRating).reversed()
                                .thenComparingLong(BestTechnicianResponse::getReviewCount).reversed()
                                .thenComparing(BestTechnicianResponse::getTechnicianId)
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    /* ---------- Helper ---------- */

    private BestTechnicianResponse toBestTech(String techId, List<Review> reviews) {

        double avg   = ratingStrategy.calculateRating(reviews);
        long   count = reviews.size();

        // Latest review preview (if any)
        Review latest = reviews.stream()
                .max(Comparator.comparing(Review::getCreatedAt))
                .orElse(null);

        String latestComment  = latest != null ? latest.getComment() : "No reviews yet.";
        String latestReviewer = latest != null
                ? userRepository.findById(latest.getUserId())
                .map(u -> u.getFullName())
                .orElse("")
                : "";

        // Look up technician’s real name; if absent we’ll filter it out later
        String fullName = userRepository.findById(techId)
                .map(u -> u.getFullName())
                .orElse("Unknown Technician");

        return new BestTechnicianResponse(
                techId,
                fullName,
                avg,
                count,
                latestComment,
                latestReviewer
        );
    }
}