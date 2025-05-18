package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponResponse {
    private String id;
    private String code;
    private String type;
    private double discountValue;
    private int maxUsage;
    private int usageCount;
}
