package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import jakarta.persistence.Entity;

@Entity
public class FixedDiscountCoupon extends Coupon {
	
	public FixedDiscountCoupon() {
		super();
    }

    public FixedDiscountCoupon(String code, double discountValue, int maxUsage) {
        super(maxUsage);
    	
    	// Validate that discount is not negative
        if (discountValue < 0) {
            throw new IllegalArgumentException("Discount must be non-negative.");
        }

        // Set initial coupon data
        setCode(code);
        setDiscountValue(discountValue);
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Use the coupon (increments usage and checks availability)
        use();

        // Apply fixed discount but make sure the result is not negative
        return Math.max(0, originalPrice - getDiscountValue());
    }
}
