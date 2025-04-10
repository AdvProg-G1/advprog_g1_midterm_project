package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public class PercentageDiscountCoupon extends Coupon {

    public PercentageDiscountCoupon(String code, double discountValue, int maxUsage) {
    }

    @Override
    public double applyDiscount(double originalPrice) {
    	return 0;
    }
}
