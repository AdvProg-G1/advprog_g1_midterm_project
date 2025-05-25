// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/model/Review.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int rating;

    @Column(length = 1000)
    private String comment;

    private String technicianId;
    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** JPA‐style all-args constructor **/
    public Review(String id,
                  int rating,
                  String comment,
                  String technicianId,
                  String userId,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.technicianId = technicianId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Test-friendly constructor matching your tests’ parameter order **/
    public Review(String id,
                  String userId,
                  String technicianId,
                  int rating,
                  String comment,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.technicianId = technicianId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}