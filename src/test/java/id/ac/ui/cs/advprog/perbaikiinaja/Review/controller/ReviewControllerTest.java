// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/controller/ReviewControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.OverallRatingService;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OverallRatingService overallRatingService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getBestEndpoint_returnsListOfBestTechnicians() throws Exception {
        BestTechnicianResponse resp = new BestTechnicianResponse(
                "tech-1","Tech One",4.5,10,"Excellent","Reviewer A"
        );

        when(overallRatingService.getTopTechnicians(3))
                .thenReturn(Collections.singletonList(resp));

        mockMvc.perform(get("/api/reviews/best")
                        .param("limit","3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].technicianId").value("tech-1"))
                .andExpect(jsonPath("$[0].fullName").value("Tech One"))
                .andExpect(jsonPath("$[0].averageRating").value(4.5))
                .andExpect(jsonPath("$[0].latestComment").value("Excellent"))
                .andExpect(jsonPath("$[0].latestReviewerName").value("Reviewer A"));

        verify(overallRatingService).getTopTechnicians(3);
    }
}