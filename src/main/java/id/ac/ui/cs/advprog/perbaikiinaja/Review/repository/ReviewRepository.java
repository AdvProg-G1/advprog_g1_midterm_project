//src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/Review/repository/ReviewRepository.java
package id.ac.ui.cs.advprog.perbaikiinaja.Review.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByTechnicianId(String technicianId);
    Review findByUserIdAndTechnicianId(String userId, String technicianId);
}