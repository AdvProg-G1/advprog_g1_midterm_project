package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class PercentageDiscountCoupon extends Coupon {

    public PercentageDiscountCoupon(String code, double discountValue, int maxUsage) {
        // Ensure discount is within valid percentage range (0 to 100)
        if (discountValue < 0 || discountValue > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }

        // Ensure the coupon can be used at least once
        if (maxUsage < 1) {
            throw new IllegalArgumentException("maxUsage must be at least 1.");
        }

        // Set coupon details
        setCode(code);
        setDiscountValue(discountValue);
        setMaxUsage(maxUsage);
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Use the coupon (increments usage and checks limit)
        use();

        // Apply the percentage discount to the original price
        return originalPrice * (1 - getDiscountValue() / 100.0);
    }
}
