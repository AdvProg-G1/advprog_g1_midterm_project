package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;

public class FixedDiscountCouponFactory implements CouponFactory {

    @Override
    public Coupon createCoupon(String code, double value, int maxUsage) {
        return new FixedDiscountCoupon(code, value, maxUsage);
    }
}
