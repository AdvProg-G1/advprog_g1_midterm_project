package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FixedDiscountCouponFactoryTest {

    // Test that a FixedDiscountCoupon is created with the correct values
    @Test
    void testCreateFixedDiscountCouponSuccessfully() {
        CouponFactory factory = new FixedDiscountCouponFactory();
        Coupon coupon = factory.createCoupon("FIXED10", 10.0, 3);

        assertTrue(coupon instanceof FixedDiscountCoupon); // Check instance type
        assertEquals("FIXED10", coupon.getCode()); // Check code
        assertEquals(10.0, coupon.getDiscountValue()); // Check discount value
        assertEquals(3, coupon.getMaxUsage()); // Check max usage
    }

    // Test that creating a coupon with invalid arguments throws an exception
    @Test
    void testCreateFixedDiscountCouponWithInvalidArgsThrows() {
        CouponFactory factory = new FixedDiscountCouponFactory();

        // Negative discount should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("INVALID", -5.0, 2);
        });

        // Zero max usage should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("INVALID", 5.0, 0);
        });
    }
}
