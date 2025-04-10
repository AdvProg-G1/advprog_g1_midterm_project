package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;

public interface CouponFactory {
	Coupon createCoupon(String code, double value, int maxUsage);
}
