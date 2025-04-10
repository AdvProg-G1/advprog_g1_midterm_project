package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FixedDiscountCouponTest {

    @Test
    void testApplyDiscountReducesPriceCorrectly() {
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX10", 10.0, 3);
        assertEquals(40.0, coupon.applyDiscount(50.0));
    }

    @Test
    void testApplyDiscountNotBelowZero() {
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX100", 100.0, 2);
        assertEquals(0.0, coupon.applyDiscount(50.0));
    }

    @Test
    void testUsageLimitExceededThrows() {
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("LIMIT", 10.0, 1);
        coupon.applyDiscount(50.0);
        assertThrows(IllegalStateException.class, () -> coupon.applyDiscount(50.0));
    }

    @Test
    void testConstructorInitializesAllValues() {
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX5", 5.0, 2);
        assertEquals("FIX5", coupon.getCode());
        assertEquals(5.0, coupon.getDiscountValue());
        assertEquals(2, coupon.getMaxUsage());
    }

    @Test
    void testInvalidConstructorThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FixedDiscountCoupon("BAD", -5.0, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FixedDiscountCoupon("BAD", 5.0, 0);
        });
    }
}
