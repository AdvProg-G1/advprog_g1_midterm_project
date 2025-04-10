package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class PercentageDiscountCoupon extends Coupon {

    public PercentageDiscountCoupon(String code, double discountValue, int maxUsage) {
        if (discountValue < 0 || discountValue > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
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
        return originalPrice * (1 - getDiscountValue() / 100.0);
    }
}
