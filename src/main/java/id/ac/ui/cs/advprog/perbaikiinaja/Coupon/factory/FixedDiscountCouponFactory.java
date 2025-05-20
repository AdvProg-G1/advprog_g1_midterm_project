package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;

// Concrete factory for creating FixedDiscountCoupon instances
public class FixedDiscountCouponFactory implements CouponFactory {

    // Creates a new FixedDiscountCoupon with the given code, value, and max usage
    @Override
    public Coupon createCoupon(String code, double value, int maxUsage) {
        return new FixedDiscountCoupon(code, value, maxUsage);
    }
}
