package id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SimpleAverageRatingStrategy implements RatingStrategy {

    @Override
    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        int sum = reviews.stream().mapToInt(Review::getRating).sum();
        return (double) sum / reviews.size();
    }
}