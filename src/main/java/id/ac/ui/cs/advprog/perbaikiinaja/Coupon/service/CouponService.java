package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponResponse;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest request);
    List<CouponResponse> getAllCoupons();
    CouponResponse getCouponById(String id);
    void deleteCoupon(String id);
    CouponResponse updateCoupon(String id, CouponRequest request);
}
