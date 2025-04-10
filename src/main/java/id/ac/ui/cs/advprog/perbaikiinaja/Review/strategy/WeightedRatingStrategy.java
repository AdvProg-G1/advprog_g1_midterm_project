package id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class WeightedRatingStrategy implements RatingStrategy {

    @Override
    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        // For example, reviews with 5 stars and a non-empty comment receive a 20% boost.
        double weightedSum = reviews.stream().mapToDouble(review -> {
            double weight = 1.0;
            if (review.getRating() == 5 && review.getComment() != null && !review.getComment().trim().isEmpty()) {
                weight = 1.2;
            }
            return review.getRating() * weight;
        }).sum();
        return weightedSum / reviews.size();
    }
}