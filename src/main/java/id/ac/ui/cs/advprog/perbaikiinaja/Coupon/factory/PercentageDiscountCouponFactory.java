package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.PercentageDiscountCoupon;

public class PercentageDiscountCouponFactory implements CouponFactory {

    @Override
    public Coupon createCoupon(String code, double value, int maxUsage) {
        return new PercentageDiscountCoupon(code, value, maxUsage);
    }
}
