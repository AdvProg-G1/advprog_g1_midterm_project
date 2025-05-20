package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CouponRequest {

	@NotNull(message = "Coupon code is required")
    private String code;
	
    @NotNull(message = "Discount value is required")
    @Min(value = 0, message = "Discount must be non-negative")
    private Double discountValue;

    @NotNull(message = "Max usage is required")
    @Min(value = 1, message = "Max usage must be at least 1")
    private Integer maxUsage;

    @NotNull(message = "Coupon type is required")
    @Pattern(regexp = "fixed|percentage", message = "Type must be 'fixed' or 'percentage'")
    private String type; // "fixed" or "percentage"
}
