//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/dto/ReviewRequest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "Technician id is required")
    private String technicianId;

    @NotBlank(message = "User id is required")
    private String userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    private String comment;
}