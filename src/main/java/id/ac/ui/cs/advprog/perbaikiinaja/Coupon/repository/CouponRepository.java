package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;
import java.util.List;

public interface CouponRepository {
    Coupon save(Coupon coupon);
    List<Coupon> findAll();
    Coupon findById(String id);
    void deleteById(String id);
    Coupon update(Coupon coupon);
}
