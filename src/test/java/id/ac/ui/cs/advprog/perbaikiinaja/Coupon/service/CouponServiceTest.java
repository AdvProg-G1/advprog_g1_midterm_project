package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CouponServiceTest {

    private CouponRepository couponRepository;
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        couponRepository = Mockito.mock(CouponRepository.class);
        couponService = new CouponServiceImpl(couponRepository);
    }

    @Test
    void testCreateFixedCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setType("fixed");
        request.setDiscountValue(20000.0);
        request.setMaxUsage(10);

        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Coupon coupon = invocation.getArgument(0);
            return coupon;
        });

        CouponResponse response = couponService.createCoupon(request);
        assertNotNull(response.getId());
        assertEquals("fixed", response.getType());
        assertEquals(20000.0, response.getDiscountValue());
        assertEquals(10, response.getMaxUsage());
    }

    @Test
    void testGetAllCouponsReturnsCorrectList() {
        List<Coupon> mockCoupons = new ArrayList<>();
        mockCoupons.add(new FixedDiscountCoupon("FIXED20", 20000.0, 5));
        mockCoupons.add(new FixedDiscountCoupon("FIXED10", 10000.0, 3));

        when(couponRepository.findAll()).thenReturn(mockCoupons);

        List<CouponResponse> responses = couponService.getAllCoupons();
        assertEquals(2, responses.size());
        assertEquals("FIXED20", responses.get(0).getCode());
    }

    @Test
    void testGetCouponByIdReturnsCoupon() {
        Coupon coupon = new FixedDiscountCoupon("FIXED5", 5000.0, 2);
        when(couponRepository.findById(coupon.getId())).thenReturn(coupon);

        CouponResponse response = couponService.getCouponById(coupon.getId());
        assertEquals("FIXED5", response.getCode());
        assertEquals(5000.0, response.getDiscountValue());
    }

    @Test
    void testDeleteCouponSuccess() {
        Coupon coupon = new FixedDiscountCoupon("DELETE10", 10000.0, 3);
        when(couponRepository.findById(coupon.getId())).thenReturn(coupon);
        doNothing().when(couponRepository).deleteById(coupon.getId());

        assertDoesNotThrow(() -> couponService.deleteCoupon(coupon.getId()));
        verify(couponRepository, times(1)).deleteById(coupon.getId());
    }

    @Test
    void testUpdateCouponSuccess() {
        Coupon coupon = new FixedDiscountCoupon("FIXED15", 15000.0, 3);
        when(couponRepository.findById(coupon.getId())).thenReturn(coupon);
        when(couponRepository.update(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CouponRequest updateRequest = new CouponRequest();
        updateRequest.setDiscountValue(17000.0);
        updateRequest.setMaxUsage(5);

        CouponResponse response = couponService.updateCoupon(coupon.getId(), updateRequest);
        assertEquals(17000.0, response.getDiscountValue());
        assertEquals(5, response.getMaxUsage());
    }
}
