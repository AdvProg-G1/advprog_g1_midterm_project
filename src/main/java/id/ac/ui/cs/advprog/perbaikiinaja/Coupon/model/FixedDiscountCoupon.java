package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class FixedDiscountCoupon extends Coupon {

    public FixedDiscountCoupon(String code, double discountValue, int maxUsage) {
    }

    @Override
    public double applyDiscount(double originalPrice) {
    	return 0;
    }
}
