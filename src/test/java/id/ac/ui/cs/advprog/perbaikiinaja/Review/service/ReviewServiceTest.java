package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = Mockito.mock(ReviewRepository.class);
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    void testCreateReviewSuccess() {
        ReviewRequest request = new ReviewRequest();
        request.setTechnicianId("tech-1");
        request.setUserId("user-1");
        request.setRating(5);
        request.setComment("Excellent service!");

        when(reviewRepository.findByUserIdAndTechnicianId("user-1", "tech-1")).thenReturn(null);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review review = invocation.getArgument(0);
            review.setId("review-1");
            review.setCreatedAt(LocalDateTime.now());
            review.setUpdatedAt(LocalDateTime.now());
            return review;
        });

        ReviewResponse response = reviewService.createReview(request);
        assertNotNull(response.getId());
        assertEquals("tech-1", response.getTechnicianId());
        assertEquals("user-1", response.getUserId());
        assertEquals(5, response.getRating());
        assertEquals("Excellent service!", response.getComment());
    }

    @Test
    void testUpdateReviewSuccess() {
        Review existing = new Review();
        existing.setId("review-1");
        existing.setRating(4);
        existing.setComment("Good service");
        existing.setTechnicianId("tech-1");
        existing.setUserId("user-1");
        existing.setCreatedAt(LocalDateTime.now());
        existing.setUpdatedAt(LocalDateTime.now());

        when(reviewRepository.findById("review-1")).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewRequest updateRequest = new ReviewRequest();
        updateRequest.setTechnicianId("tech-1");
        updateRequest.setUserId("user-1");
        updateRequest.setRating(5);
        updateRequest.setComment("Excellent service!");

        ReviewResponse response = reviewService.updateReview("review-1", updateRequest);
        assertEquals(5, response.getRating());
        assertEquals("Excellent service!", response.getComment());
    }

    @Test
    void testDeleteReviewSuccess() {
        Review existing = new Review();
        existing.setId("review-1");
        existing.setUserId("user-1");

        when(reviewRepository.findById("review-1")).thenReturn(Optional.of(existing));
        doNothing().when(reviewRepository).delete(existing);

        assertDoesNotThrow(() -> reviewService.deleteReview("review-1", "user-1"));
        verify(reviewRepository, times(1)).delete(existing);
    }

    @Test
    void testGetReviewsForTechnician() {
        // Correct parameter order: id, rating, comment, technicianId, userId, createdAt, updatedAt.
        Review review1 = new Review("review-1", 5, "Great!", "tech-1", "user-1", LocalDateTime.now(), LocalDateTime.now());
        Review review2 = new Review("review-2", 4, "Good", "tech-1", "user-2", LocalDateTime.now(), LocalDateTime.now());
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        when(reviewRepository.findByTechnicianId("tech-1")).thenReturn(reviews);

        List<ReviewResponse> responses = reviewService.getReviewsForTechnician("tech-1");
        assertEquals(2, responses.size());
    }
}