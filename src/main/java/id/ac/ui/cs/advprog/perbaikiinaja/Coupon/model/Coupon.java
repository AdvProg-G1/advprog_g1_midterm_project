package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model;

public abstract class Coupon {

    private String code;
    private double discountValue;
    private int maxUsage;
    private int usageCount = 0;

    // Returns true if the coupon can still be used
    public boolean isAvailable() {
        return usageCount < maxUsage;
    }

    // Increments usage count when the coupon is used
    protected void use() {
        if (!isAvailable()) {
            throw new IllegalStateException("Coupon usage limit exceeded");
        }
        usageCount++;
    }


    // Get the number of times the coupon has been used
    public int getUsageCount() {
        return usageCount;
    }

    // Getters and setters for code
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getters and setters for maxUsage
    public int getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }

    // Getters and setters for discountValue
    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    // Abstract method to apply discount
    public abstract double applyDiscount(double originalPrice);
}
