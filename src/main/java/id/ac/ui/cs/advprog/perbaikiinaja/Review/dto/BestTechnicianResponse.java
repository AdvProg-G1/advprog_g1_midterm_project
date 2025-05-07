// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/dto/BestTechnicianResponse.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestTechnicianResponse {
    private String technicianId;
    private String fullName;
    private double averageRating;
    private long reviewCount;
    private String latestComment;
    private String latestReviewerName;
}