package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class FixedDiscountCoupon extends Coupon {

    public FixedDiscountCoupon(String code, double discountValue, int maxUsage) {
        // Validate that discount is not negative
        if (discountValue < 0) {
            throw new IllegalArgumentException("Discount must be non-negative.");
        }

        // Validate that max usage is at least 1
        if (maxUsage < 1) {
            throw new IllegalArgumentException("maxUsage must be at least 1.");
        }

        // Set initial coupon data
        setCode(code);
        setDiscountValue(discountValue);
        setMaxUsage(maxUsage);
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Use the coupon (increments usage and checks availability)
        use();

        // Apply fixed discount but make sure the result is not negative
        return Math.max(0, originalPrice - getDiscountValue());
    }
}
