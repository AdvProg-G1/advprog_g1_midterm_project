package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy.RatingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OverallRatingServiceTest {

    private ReviewRepository reviewRepository;
    private RatingStrategy ratingStrategy;
    private OverallRatingService overallRatingService;

    @BeforeEach
    void setUp() {
        reviewRepository = Mockito.mock(ReviewRepository.class);
        ratingStrategy = Mockito.mock(RatingStrategy.class);
        // Create OverallRatingService using its constructor
        overallRatingService = new OverallRatingService(reviewRepository, ratingStrategy);
    }

    @Test
    void testCalculateOverallRating() {
        Review review1 = new Review("id1", 5, "Excellent", "tech1", "user1", LocalDateTime.now(), LocalDateTime.now());
        Review review2 = new Review("id2", 3, "Average", "tech1", "user2", LocalDateTime.now(), LocalDateTime.now());
        List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewRepository.findByTechnicianId("tech1")).thenReturn(reviews);
        // Simulate that the chosen strategy returns an overall rating of 4.0
        when(ratingStrategy.calculateRating(reviews)).thenReturn(4.0);

        double overallRating = overallRatingService.calculateOverallRating("tech1");
        assertEquals(4.0, overallRating);

        verify(reviewRepository, times(1)).findByTechnicianId("tech1");
        verify(ratingStrategy, times(1)).calculateRating(reviews);
    }
}