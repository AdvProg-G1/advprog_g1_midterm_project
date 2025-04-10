package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.PercentageDiscountCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountCouponFactoryTest {

    // Test that a PercentageDiscountCoupon is created with the correct values
    @Test
    void testCreatePercentageDiscountCouponSuccessfully() {
        CouponFactory factory = new PercentageDiscountCouponFactory();
        Coupon coupon = factory.createCoupon("PERC20", 20.0, 2);

        assertTrue(coupon instanceof PercentageDiscountCoupon); // Check type
        assertEquals("PERC20", coupon.getCode()); // Check code
        assertEquals(20.0, coupon.getDiscountValue()); // Check discount value
        assertEquals(2, coupon.getMaxUsage()); // Check max usage
    }

    // Test that creating a coupon with invalid values throws an exception
    @Test
    void testCreatePercentageDiscountCouponWithInvalidArgsThrows() {
        CouponFactory factory = new PercentageDiscountCouponFactory();

        // Negative discount should not be allowed
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", -10.0, 2);
        });

        // Discount over 100% should not be allowed
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", 120.0, 2);
        });

        // Zero max usage should not be allowed
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", 10.0, 0);
        });
    }
}
