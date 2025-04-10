package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.factory;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.PercentageDiscountCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountCouponFactoryTest {

    @Test
    void testCreatePercentageDiscountCouponSuccessfully() {
        CouponFactory factory = new PercentageDiscountCouponFactory();
        Coupon coupon = factory.createCoupon("PERC20", 20.0, 2);

        assertTrue(coupon instanceof PercentageDiscountCoupon);
        assertEquals("PERC20", coupon.getCode());
        assertEquals(20.0, coupon.getDiscountValue());
        assertEquals(2, coupon.getMaxUsage());
    }

    @Test
    void testCreatePercentageDiscountCouponWithInvalidArgsThrows() {
        CouponFactory factory = new PercentageDiscountCouponFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", -10.0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", 120.0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCoupon("BAD", 10.0, 0);
        });
    }
}
