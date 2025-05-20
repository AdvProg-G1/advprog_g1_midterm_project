package id.ac.ui.cs.advprog.perbaikiinaja.Review.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.dto.BestTechnicianResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.OverallRatingService;
import id.ac.ui.cs.advprog.perbaikiinaja.Review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OverallRatingService overallRatingService;

    @Test
    void getBestEndpoint_returnsListOfBestTechnicians() throws Exception {
        // Given
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

        // When & Then
        mockMvc.perform(get("/api/reviews/best")      // <-- adjust path to match your @RequestMapping
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
}
