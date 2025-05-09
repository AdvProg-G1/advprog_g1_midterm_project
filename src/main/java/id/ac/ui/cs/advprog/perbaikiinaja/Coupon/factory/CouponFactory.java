package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;

// Interface for coupon factories
// Defines a common method to create a coupon with a code, value, and usage limit
public interface CouponFactory {
    Coupon createCoupon(String code, double value, int maxUsage);
}
