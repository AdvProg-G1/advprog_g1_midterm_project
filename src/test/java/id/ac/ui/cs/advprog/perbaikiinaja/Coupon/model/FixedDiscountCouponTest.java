package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FixedDiscountCouponTest {

    @Test
    void testApplyDiscountReducesPriceCorrectly() {
        // A fixed discount of 10 should reduce 50 to 40
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX10", 10.0, 3);
        assertEquals(40.0, coupon.applyDiscount(50.0));
    }

    @Test
    void testApplyDiscountNotBelowZero() {
        // Discount should not make the final price negative
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX100", 100.0, 2);
        assertEquals(0.0, coupon.applyDiscount(50.0));
    }

    @Test
    void testUsageLimitExceededThrows() {
        // Using the coupon more than maxUsage should throw an error
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("LIMIT", 10.0, 1);
        coupon.applyDiscount(50.0); // First use is fine
        assertThrows(IllegalStateException.class, () -> coupon.applyDiscount(50.0)); // Second should fail
    }

    @Test
    void testConstructorInitializesAllValues() {
        // Constructor should correctly set all fields
        FixedDiscountCoupon coupon = new FixedDiscountCoupon("FIX5", 5.0, 2);
        assertEquals("FIX5", coupon.getCode());
        assertEquals(5.0, coupon.getDiscountValue());
        assertEquals(2, coupon.getMaxUsage());
    }

    @Test
    void testInvalidConstructorThrows() {
        // Constructor should reject negative discount or invalid usage count
        assertThrows(IllegalArgumentException.class, () -> {
            new FixedDiscountCoupon("BAD", -5.0, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FixedDiscountCoupon("BAD", 5.0, 0);
        });
    }
}
