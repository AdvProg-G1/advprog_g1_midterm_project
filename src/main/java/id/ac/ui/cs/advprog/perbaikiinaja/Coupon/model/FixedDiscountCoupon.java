package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class FixedDiscountCoupon extends Coupon {

    public FixedDiscountCoupon(String code, double discountValue, int maxUsage) {
        if (discountValue < 0) {
            throw new IllegalArgumentException("Discount must be non-negative.");
        }
        if (maxUsage < 1) {
            throw new IllegalArgumentException("maxUsage must be at least 1.");
        }

        setCode(code);
        setDiscountValue(discountValue);
        setMaxUsage(maxUsage);
    }

    @Override
    public double applyDiscount(double originalPrice) {
        use();
        return Math.max(0, originalPrice - getDiscountValue());
    }
}
