package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountCouponTest {

    @Test
    void testApplyDiscountReducesPriceCorrectly() {
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("PROMO20", 20.0, 2);
        assertEquals(80.0, coupon.applyDiscount(100.0));
    }

    @Test
    void testApplyDiscountWithZeroPercent() {
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("ZERO", 0.0, 1);
        assertEquals(100.0, coupon.applyDiscount(100.0));
    }

    @Test
    void testUsageLimitExceededThrows() {
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("ONCE", 50.0, 1);
        coupon.applyDiscount(100.0);
        assertThrows(IllegalStateException.class, () -> coupon.applyDiscount(100.0));
    }

    @Test
    void testConstructorInitializesValues() {
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("PROMO", 30.0, 4);
        assertEquals("PROMO", coupon.getCode());
        assertEquals(30.0, coupon.getDiscountValue());
        assertEquals(4, coupon.getMaxUsage());
    }

    @Test
    void testInvalidConstructorThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", -10.0, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", 150.0, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", 10.0, 0);
        });
    }
}
