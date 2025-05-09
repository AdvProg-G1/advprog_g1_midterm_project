package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.PercentageDiscountCoupon;

// Concrete factory for creating PercentageDiscountCoupon instances
public class PercentageDiscountCouponFactory implements CouponFactory {

    // Creates a new PercentageDiscountCoupon with given code, value, and max usage
    @Override
    public Coupon createCoupon(String code, double value, int maxUsage) {
        return new PercentageDiscountCoupon(code, value, maxUsage);
    }
}
