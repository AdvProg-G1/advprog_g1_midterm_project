package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;


    @Override
    public CouponResponse createCoupon(CouponRequest request) {
        return null;
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return null; 
    }

    @Override
    public CouponResponse getCouponById(String id) {
        return null;
    }

    @Override
    public void deleteCoupon(String id) {
 
    }

    @Override
    public CouponResponse updateCoupon(String id, CouponRequest request) {
        return null; 
    }
}
