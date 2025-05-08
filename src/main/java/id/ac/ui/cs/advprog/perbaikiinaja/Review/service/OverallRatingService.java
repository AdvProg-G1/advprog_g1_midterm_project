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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OverallRatingService {

    private final ReviewRepository reviewRepository;
    private final RatingStrategy ratingStrategy;
    private final UserRepository userRepository;

    public double calculateOverallRating(String techId) {
        List<Review> reviews = reviewRepository.findByTechnicianId(techId);
        return ratingStrategy.calculateRating(reviews);
    }

    public List<BestTechnicianResponse> getTopTechnicians(int limit) {
        Set<String> techIds = reviewRepository.findAll().stream()
                .map(Review::getTechnicianId)
                .collect(Collectors.toSet());

        return techIds.stream()
                .map(techId -> {
                    List<Review> reviews = reviewRepository.findByTechnicianId(techId);
                    double avg = ratingStrategy.calculateRating(reviews);
                    Review latest = reviews.stream()
                            .max(Comparator.comparing(Review::getCreatedAt))
                            .orElse(null);

                    String comment = latest != null
                            ? latest.getComment()
                            : "No reviews yet.";
                    String reviewer = (latest != null)
                            ? userRepository.findById(latest.getUserId())
                            .map(u -> u.getFullName())
                            .orElse("")
                            : "";
                    String fullName = userRepository.findById(techId)
                            .map(u -> u.getFullName())
                            .orElse("Unknown Technician");

                    return new BestTechnicianResponse(
                            techId,
                            fullName,
                            avg,
                            reviews.size(),
                            comment,
                            reviewer
                    );
                })
                .sorted(
                        Comparator.comparingDouble(BestTechnicianResponse::getAverageRating)
                                .reversed()
                                .thenComparing(BestTechnicianResponse::getTechnicianId)
                )
                .limit(limit)
                .collect(Collectors.toList());
    }
}