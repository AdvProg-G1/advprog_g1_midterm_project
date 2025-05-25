// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/service/OverallRatingServiceTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.repository.ReviewRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy.RatingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OverallRatingServiceTest {

    private ReviewRepository  reviewRepository;
    private RatingStrategy    ratingStrategy;
    private UserRepository    userRepository;
    private OverallRatingService service;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        ratingStrategy   = mock(RatingStrategy.class);
        userRepository   = mock(UserRepository.class);
        service          = new OverallRatingService(reviewRepository, ratingStrategy, userRepository);
    }

    @Test
    void calculateOverallRating_delegatesToStrategy() {
        String techId = "tech-1";
        List<Review> reviews = Collections.singletonList(
                new Review("r1","u1","tech-1",5,"Good",
                        LocalDateTime.now(), LocalDateTime.now())
        );

        when(reviewRepository.findByTechnicianId(techId)).thenReturn(reviews);
        when(ratingStrategy.calculateRating(reviews)).thenReturn(4.5);

        double result = service.calculateOverallRating(techId);

        assertThat(result).isEqualTo(4.5);
        verify(ratingStrategy).calculateRating(reviews);
    }

    @Test
    void getTopTechnicians_filtersOutUnknownTech_andBuildsCorrectResponse() {

        // ─── sample reviews ────────────────────────────────────────────────────
        Review r1 = new Review("r1","userA","tech-1",5,"Great",
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2));
        Review r2 = new Review("r2","userB","tech-1",3,"Ok",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
        Review r3 = new Review("r3","userC","tech-2",4,"Nice",
                LocalDateTime.now(),             LocalDateTime.now());

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(r1, r2, r3));
        when(ratingStrategy.calculateRating(Arrays.asList(r1, r2))).thenReturn(4.0);

        // ─── mock existing technician (tech-1) ───────────────────────────────
        User techOne = new User();
        techOne.setId("tech-1");
        techOne.setFullName("Tech One");
        techOne.setRole("technician");
        when(userRepository.findById("tech-1")).thenReturn(Optional.of(techOne));

        // tech-2 does NOT exist → filtered out
        when(userRepository.findById("tech-2")).thenReturn(Optional.empty());

        // ─── mock latest reviewer (userB) ─────────────────────────────────────
        User reviewerB = new User();
        reviewerB.setId("userB");
        reviewerB.setFullName("Reviewer B");
        when(userRepository.findById("userB")).thenReturn(Optional.of(reviewerB));

        // ─── exercise ─────────────────────────────────────────────────────────
        List<BestTechnicianResponse> top = service.getTopTechnicians(5);

        // ─── verify ───────────────────────────────────────────────────────────
        assertThat(top).hasSize(1);                 // tech-2 filtered out

        BestTechnicianResponse best = top.get(0);
        assertThat(best.getTechnicianId()).isEqualTo("tech-1");
        assertThat(best.getFullName()).isEqualTo("Tech One");
        assertThat(best.getAverageRating()).isEqualTo(4.0);
        assertThat(best.getReviewCount()).isEqualTo(2);
        assertThat(best.getLatestComment()).isEqualTo("Ok");
        assertThat(best.getLatestReviewerName()).isEqualTo("Reviewer B");
    }
}