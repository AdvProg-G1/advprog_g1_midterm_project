package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import jakarta.persistence.Entity;

@Entity
public class PercentageDiscountCoupon extends Coupon {

	public PercentageDiscountCoupon() {
		super();
	}
    public PercentageDiscountCoupon(String code, double discountValue, int maxUsage) {
    	super(maxUsage);
    	
    	// Ensure discount is within valid percentage range (0 to 100)
        if (discountValue < 0 || discountValue > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }

        // Set coupon details
        setCode(code);
        setDiscountValue(discountValue);
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Use the coupon (increments usage and checks limit)
        use();

        // Apply the percentage discount to the original price
        return originalPrice * (1 - getDiscountValue() / 100.0);
    }
	@Override
	public String getType() {
		return "percentage";
	}
}
