// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/service/ReviewServiceTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
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
    private UserRepository userRepository;
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = Mockito.mock(ReviewRepository.class);
        userRepository   = Mockito.mock(UserRepository.class);
        // make findById(...) return Optional.empty() so mapToResponse falls back
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        reviewService = new ReviewServiceImpl(reviewRepository, userRepository);
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
        assertEquals("tech-1",       response.getTechnicianId());
        assertEquals("user-1",       response.getUserId());
        assertEquals(5,              response.getRating());
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
        assertEquals(5,  response.getRating());
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
        LocalDateTime now = LocalDateTime.now();

        Review review1 = new Review();
        review1.setId("review-1");
        review1.setRating(5);
        review1.setComment("Great!");
        review1.setTechnicianId("tech-1");
        review1.setUserId("user-1");
        review1.setCreatedAt(now);
        review1.setUpdatedAt(now);

        Review review2 = new Review();
        review2.setId("review-2");
        review2.setRating(4);
        review2.setComment("Good");
        review2.setTechnicianId("tech-1");
        review2.setUserId("user-2");
        review2.setCreatedAt(now);
        review2.setUpdatedAt(now);

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        when(reviewRepository.findByTechnicianId("tech-1")).thenReturn(reviews);

        List<ReviewResponse> responses = reviewService.getReviewsForTechnician("tech-1");
        assertEquals(2, responses.size());
    }
}