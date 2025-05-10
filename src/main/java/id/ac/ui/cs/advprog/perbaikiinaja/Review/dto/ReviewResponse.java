// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/dto/ReviewResponse.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String technicianId;
    private String userId;
    private int rating;
    private String comment;
    private String reviewerName;
}