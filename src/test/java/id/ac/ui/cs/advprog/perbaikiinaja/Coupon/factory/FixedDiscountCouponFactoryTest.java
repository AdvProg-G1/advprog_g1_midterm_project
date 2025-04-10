package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.FixedDiscountCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FixedDiscountCouponFactoryTest {

    @Test
    void testCreateFixedDiscountCouponSuccessfully() {
        CouponFactory factory = new FixedDiscountCouponFactory();
        Coupon coupon = factory.createCoupon("FIXED10", 10.0, 3);

        assertTrue(coupon instanceof FixedDiscountCoupon);
        assertEquals("FIXED10", coupon.getCode());
        assertEquals(10.0, coupon.getDiscountValue());
        assertEquals(3, coupon.getMaxUsage());
    }

    @Test
    void testCreateFixedDiscountCouponWithInvalidArgsThrows() {
        CouponFactory factory = new FixedDiscountCouponFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("INVALID", -5.0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("INVALID", 5.0, 0);
        });
    }
}
