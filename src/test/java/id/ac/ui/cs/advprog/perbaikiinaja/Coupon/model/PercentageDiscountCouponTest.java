package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountCouponTest {

    @Test
    void testApplyDiscountReducesPriceCorrectly() {
        // A 20% discount on 100 should result in 80
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("PROMO20", 20.0, 2);
        assertEquals(80.0, coupon.applyDiscount(100.0));
    }

    @Test
    void testApplyDiscountWithZeroPercent() {
        // A 0% discount should not reduce the price
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("ZERO", 0.0, 1);
        assertEquals(100.0, coupon.applyDiscount(100.0));
    }

    @Test
    void testUsageLimitExceededThrows() {
        // Should throw an exception when used more than the allowed number of times
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("ONCE", 50.0, 1);
        coupon.applyDiscount(100.0); // First use is valid
        assertThrows(IllegalStateException.class, () -> coupon.applyDiscount(100.0)); // Second use should fail
    }

    @Test
    void testConstructorInitializesValues() {
        // Constructor should correctly initialize all attributes
        PercentageDiscountCoupon coupon = new PercentageDiscountCoupon("PROMO", 30.0, 4);
        assertEquals("PROMO", coupon.getCode());
        assertEquals(30.0, coupon.getDiscountValue());
        assertEquals(4, coupon.getMaxUsage());
    }

    @Test
    void testInvalidConstructorThrows() {
        // Constructor should reject invalid discount values and maxUsage
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", -10.0, 2); // Negative discount
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", 150.0, 2); // Discount over 100%
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PercentageDiscountCoupon("BAD", 10.0, 0); // Invalid maxUsage
        });
    }
}
