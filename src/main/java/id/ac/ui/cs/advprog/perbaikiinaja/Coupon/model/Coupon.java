package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public abstract class Coupon {
	
	@Id
	private final String id; // Unique identifier for the coupon
    private String code;
    private double discountValue;
    private int maxUsage;
    private int usageCount = 0;

    public Coupon(int maxUsage) {
        this.id = UUID.randomUUID().toString(); // Generate unique ID on creation
        
        // Ensure the coupon can be used at least once
        if (maxUsage < 1) {
            throw new IllegalArgumentException("maxUsage must be at least 1.");
        }
        
        setMaxUsage(maxUsage);
    }

    
    // Returns true if the coupon can still be used
    public boolean isAvailable() {
        return usageCount < maxUsage;
    }

    // Increments usage count when the coupon is used
    protected void use() {
        if (!isAvailable()) {
            throw new IllegalStateException("Coupon usage limit exceeded");
        }
        usageCount++;
    }


    // Get the number of times the coupon has been used
    public int getUsageCount() {
        return usageCount;
    }

    // Getters and setters for code
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getters and setters for maxUsage
    public int getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }

    // Getters and setters for discountValue
    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
    
    // Get the UUID of the coupon
    public String getId() {
        return id;
    }

    // Abstract method to apply discount
    public abstract double applyDiscount(double originalPrice);
}
