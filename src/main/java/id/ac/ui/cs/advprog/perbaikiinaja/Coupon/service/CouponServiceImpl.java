package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.controller.CouponNotificationController;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.PercentageDiscountCoupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    
    @Autowired
    private final CouponNotificationController notificationController;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {
    	String code = request.getCode();
    	
    	if (code == null || code.isBlank()) {
            code = "CPN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

    	
        Coupon coupon;
        switch (request.getType().toLowerCase()) {
            case "fixed":
                coupon = new FixedDiscountCoupon(code, request.getDiscountValue(), request.getMaxUsage());
                break;
            case "percentage":
                coupon = new PercentageDiscountCoupon(code, request.getDiscountValue(), request.getMaxUsage());
                break;
            default:
                throw new IllegalArgumentException("Invalid coupon type: " + request.getType());
        }

        couponRepository.save(coupon);
        notificationController.notifyCouponListChanged();
        return mapToResponse(coupon);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponse getCouponById(String id) {
    	Coupon coupon = getCouponOrThrow(id);
    	return mapToResponse(coupon);

    }

    @Override
    public void deleteCoupon(String id) {
    	Coupon coupon = getCouponOrThrow(id);
    	couponRepository.deleteById(id);   
    	notificationController.notifyCouponListChanged();
    }

    @Override
    public CouponResponse updateCoupon(String id, CouponRequest request) {
    	Coupon coupon = getCouponOrThrow(id);

    	coupon.setDiscountValue(request.getDiscountValue());
    	coupon.setMaxUsage(request.getMaxUsage());

    	Coupon updated = couponRepository.save(coupon);  
    	notificationController.notifyCouponListChanged();
    	return mapToResponse(updated);
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        String type = coupon.getType();
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                type,
                coupon.getDiscountValue(),
                coupon.getMaxUsage(),
                coupon.getUsageCount()
        );
    }
    
    private Coupon getCouponOrThrow(String id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
    }

}
