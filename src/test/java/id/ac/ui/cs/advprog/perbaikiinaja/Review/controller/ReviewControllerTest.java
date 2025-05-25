// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/controller/ReviewControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.controller.ReviewController;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.ReviewResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.OverallRatingService;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OverallRatingService overallRatingService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void getBestEndpoint_returnsListOfBestTechnicians() throws Exception {
        BestTechnicianResponse resp = new BestTechnicianResponse(
                "tech-1",
                "Tech One",
                4.5,
                10,
                "Excellent",
                "Reviewer A"
        );
        when(overallRatingService.getTopTechnicians(3))
                .thenReturn(Collections.singletonList(resp));

        mockMvc.perform(get("/api/reviews/best")
                        .param("limit", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].technicianId").value("tech-1"))
                .andExpect(jsonPath("$[0].fullName").value("Tech One"))
                .andExpect(jsonPath("$[0].averageRating").value(4.5))
                .andExpect(jsonPath("$[0].latestComment").value("Excellent"))
                .andExpect(jsonPath("$[0].latestReviewerName").value("Reviewer A"));
    }

    @Test
    void getOne_returnsReview() throws Exception {
        String reviewId = "rev-1";
        // swap userId & technicianId to match constructor order: (id, technicianId, userId, rating, comment, reviewerName)
        ReviewResponse resp = new ReviewResponse(
                reviewId,
                "tech-1",
                "user-1",
                5,
                "Great job",
                ""  // reviewerName can be empty for test
        );
        when(reviewService.getReviewById(reviewId)).thenReturn(resp);

        mockMvc.perform(get("/api/reviews/{id}", reviewId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.technicianId").value("tech-1"))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great job"));
    }

    @Test
    void byTechnician_returnsReviewsForTechnician() throws Exception {
        String techId = "tech-2";
        // ensure technicianId is first, then userId
        ReviewResponse resp = new ReviewResponse(
                "rev-2",
                techId,
                "user-2",
                4,
                "Very good",
                ""
        );
        when(reviewService.getReviewsForTechnician(techId))
                .thenReturn(Collections.singletonList(resp));

        mockMvc.perform(get("/api/reviews/technician/{technicianId}", techId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].technicianId").value(techId))
                .andExpect(jsonPath("$[0].userId").value("user-2"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[0].comment").value("Very good"));
    }

    @Test
    void deleteEndpoint_deletesReview() throws Exception {
        String reviewId = "rev-3";
        String userId = "user-3";
        doNothing().when(reviewService).deleteReview(reviewId, userId);

        mockMvc.perform(delete("/api/reviews/{id}", reviewId)
                        .param("userId", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_returnsCreatedReview() throws Exception {
        ReviewRequest req = new ReviewRequest();
        req.setUserId("user-4");
        req.setTechnicianId("tech-3");
        req.setRating(3);
        req.setComment("Good");

        ReviewResponse resp = new ReviewResponse(
                "rev-4",
                "tech-3",
                "user-4",
                3,
                "Good",
                ""
        );
        when(reviewService.createReview(req)).thenReturn(resp);

        mockMvc.perform(post("/api/reviews")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("rev-4"))
                .andExpect(jsonPath("$.technicianId").value("tech-3"))
                .andExpect(jsonPath("$.userId").value("user-4"))
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.comment").value("Good"));
    }

    @Test
    void update_returnsUpdatedReview() throws Exception {
        String reviewId = "rev-5";
        ReviewRequest req = new ReviewRequest();
        req.setUserId("user-5");
        req.setTechnicianId("tech-4");
        req.setRating(2);
        req.setComment("Okay");

        ReviewResponse resp = new ReviewResponse(
                reviewId,
                "tech-4",
                "user-5",
                2,
                "Okay",
                ""
        );
        when(reviewService.updateReview(reviewId, req)).thenReturn(resp);

        mockMvc.perform(put("/api/reviews/{id}", reviewId)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.technicianId").value("tech-4"))
                .andExpect(jsonPath("$.userId").value("user-5"))
                .andExpect(jsonPath("$.rating").value(2))
                .andExpect(jsonPath("$.comment").value("Okay"));
    }
}