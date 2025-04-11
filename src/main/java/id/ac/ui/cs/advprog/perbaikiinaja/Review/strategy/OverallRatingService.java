package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy.RatingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

// Skeleton service to compute the overall rating using a RatingStrategy
@Service
@RequiredArgsConstructor
public class OverallRatingService {

    private final ReviewRepository reviewRepository;
    private final RatingStrategy ratingStrategy; // Will be injected by Spring

    public double calculateOverallRating(String technicianId) {
        List<Review> reviews = reviewRepository.findByTechnicianId(technicianId);
        return ratingStrategy.calculateRating(reviews);
    }
}