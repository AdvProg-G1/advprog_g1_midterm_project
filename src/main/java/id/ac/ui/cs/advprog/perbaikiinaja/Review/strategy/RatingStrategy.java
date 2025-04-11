package id.ac.ui.cs.advprog.perbaikiinaja.Review.strategy;

import id.ac.ui.cs.advprog.perbaikiinaja.Review.model.Review;
import java.util.List;

// Skeleton interface for overall rating calculation using the Strategy pattern
public interface RatingStrategy {
    double calculateRating(List<Review> reviews);
}