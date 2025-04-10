package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private Coupon coupon;

    // Set up a default coupon instance before each test
    @BeforeEach
    void setUp() {
        coupon = new FixedDiscountCoupon();
        coupon.setCode("TESTCODE");
        coupon.setDiscountValue(10);
        coupon.setMaxUsage(2);
    }

    // Test that the coupon is indeed initially available
    @Test
    void testIsAvailableInitiallyTrue() {
        assertTrue(coupon.isAvailable());
    }

    // Test that the coupon becomes unavailable after reaching its max usage
    @Test
    void testIsNotAvailableAfterAllUsages() {
    	coupon.applyDiscount(0);
    	coupon.applyDiscount(0);
        assertFalse(coupon.isAvailable());
    }

    // Test that using the coupon increments the usage count
    @Test
    void testUseCount() {
        coupon.applyDiscount(0);
        
        assertFalse(coupon.getUsageCount() == 0);
    }

    // Test the setter for the coupon code
    @Test
    void testSetCode() {
        coupon.setCode("ABC123");
        assertEquals("ABC123", coupon.getCode());
    }
    
    // Test the getter for the coupon code
    @Test
    void testGetCode() {
        String result = coupon.getCode();
        assertEquals("TESTCODE", result);
    }

    // Test the setter for the discount value
    @Test
    void testSetDiscountValue() {
        coupon.setDiscountValue(15.0);
        assertEquals(15.0, coupon.getDiscountValue());
    }
 
    // Test the getter for the discount value
    @Test
    void testGetDiscountValue() {
        double result = coupon.getDiscountValue();
        assertEquals(10.0, result);
    }

    // Test the setter for the maximum usage
    @Test
    void testSetMaxUsage() {
        coupon.setMaxUsage(5);
        assertEquals(5, coupon.getMaxUsage());
    }

    // Test the getter for the maximum usage
    @Test
    void testGetMaxUsage() {
        int result = coupon.getMaxUsage();
        assertEquals(2, result);
    }
}

