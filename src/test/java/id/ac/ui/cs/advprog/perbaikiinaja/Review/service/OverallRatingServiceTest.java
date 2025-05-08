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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OverallRatingServiceTest {

    private ReviewRepository reviewRepository;
    private RatingStrategy ratingStrategy;
    private UserRepository userRepository;
    private OverallRatingService service;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        ratingStrategy   = mock(RatingStrategy.class);
        userRepository   = mock(UserRepository.class);
        service = new OverallRatingService(reviewRepository, ratingStrategy, userRepository);
    }

    @Test
    void calculateOverallRating_delegatesToStrategy() {
        String techId = "tech-1";
        List<Review> reviews = Collections.singletonList(
                new Review("r1", "u1", "tech-1", 5, "Good",
                        LocalDateTime.now(), LocalDateTime.now())
        );

        when(reviewRepository.findByTechnicianId(techId)).thenReturn(reviews);
        when(ratingStrategy.calculateRating(reviews)).thenReturn(4.5);

        double result = service.calculateOverallRating(techId);

        assertThat(result).isEqualTo(4.5);
        verify(ratingStrategy).calculateRating(reviews);
    }

    @Test
    void getTopTechnicians_buildsCorrectResponse() {
        Review r1 = new Review("r1", "userA", "tech-1", 5, "Great",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2));
        Review r2 = new Review("r2", "userB", "tech-1", 3, "Ok",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1));
        Review r3 = new Review("r3", "userC", "tech-2", 4, "Nice",
                LocalDateTime.now(), LocalDateTime.now());

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(r1, r2, r3));
        when(reviewRepository.findByTechnicianId("tech-1"))
                .thenReturn(Arrays.asList(r1, r2));
        when(reviewRepository.findByTechnicianId("tech-2"))
                .thenReturn(Collections.singletonList(r3));

        when(ratingStrategy.calculateRating(Arrays.asList(r1, r2))).thenReturn(4.0);
        when(ratingStrategy.calculateRating(Collections.singletonList(r3))).thenReturn(4.0);

        // Updated to use 7-arg constructor (with role)
        when(userRepository.findById("tech-1"))
                .thenReturn(Optional.of(new User(
                        "tech-1",
                        "Tech One",
                        "",      // email
                        "",      // password
                        "",      // phone
                        "",      // address
                        "ROLE_TECHNICIAN"
                )));
        when(userRepository.findById("tech-2"))
                .thenReturn(Optional.empty());
        when(userRepository.findById("userB"))
                .thenReturn(Optional.of(new User(
                        "userB",
                        "Reviewer B",
                        "",      // email
                        "",      // password
                        "",      // phone
                        "",      // address
                        "ROLE_USER"
                )));

        List<BestTechnicianResponse> top = service.getTopTechnicians(2);

        assertThat(top).hasSize(2);

        BestTechnicianResponse first = top.get(0);
        assertThat(first.getTechnicianId()).isEqualTo("tech-1");
        assertThat(first.getFullName()).isEqualTo("Tech One");
        assertThat(first.getAverageRating()).isEqualTo(4.0);
        assertThat(first.getLatestComment()).isEqualTo("Ok");
        assertThat(first.getLatestReviewerName()).isEqualTo("Reviewer B");

        BestTechnicianResponse second = top.get(1);
        assertThat(second.getTechnicianId()).isEqualTo("tech-2");
        assertThat(second.getFullName()).isEqualTo("Unknown Technician");
        assertThat(second.getLatestComment()).isEqualTo("Nice");
        assertThat(second.getLatestReviewerName()).isEqualTo("");
    }
}
