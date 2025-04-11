package id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RatingStrategyTest {

    @Test
    public void testSimpleAverageRatingStrategy_withEmptyList_returnsZero() {
        RatingStrategy strategy = new SimpleAverageRatingStrategy();
        List<Review> reviews = Collections.emptyList();
        double rating = strategy.calculateRating(reviews);
        assertEquals(0, rating);
    }

    @Test
    public void testSimpleAverageRatingStrategy_calculatesCorrectAverage() {
        RatingStrategy strategy = new SimpleAverageRatingStrategy();
        Review review1 = new Review("id1", 5, "Excellent", "tech1", "user1", LocalDateTime.now(), LocalDateTime.now());
        Review review2 = new Review("id2", 3, "Good", "tech1", "user2", LocalDateTime.now(), LocalDateTime.now());
        List<Review> reviews = Arrays.asList(review1, review2);
        double rating = strategy.calculateRating(reviews);
        // Expected average: (5 + 3) / 2 = 4.0
        assertEquals(4.0, rating);
    }

    @Test
    public void testWeightedRatingStrategy_withEmptyList_returnsZero() {
        RatingStrategy strategy = new WeightedRatingStrategy();
        List<Review> reviews = Collections.emptyList();
        double rating = strategy.calculateRating(reviews);
        assertEquals(0, rating);
    }

    @Test
    public void testWeightedRatingStrategy_appliesBoost() {
        RatingStrategy strategy = new WeightedRatingStrategy();
        // Create two reviews: one eligible for boost, one not.
        Review review1 = new Review("id1", 5, "Excellent service!", "tech1", "user1", LocalDateTime.now(), LocalDateTime.now());
        Review review2 = new Review("id2", 4, "Good", "tech1", "user2", LocalDateTime.now(), LocalDateTime.now());
        List<Review> reviews = Arrays.asList(review1, review2);

        double rating = strategy.calculateRating(reviews);
        // Calculation:
        // review1: 5 * 1.2 = 6, review2: 4 * 1.0 = 4, average = (6 + 4) / 2 = 5.0
        assertEquals(5.0, rating);
    }
}