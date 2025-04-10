//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/model/Review.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int rating;

    @Column(length = 1000)
    private String comment;

    // Technician and User foreign keys
    private String technicianId;
    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}