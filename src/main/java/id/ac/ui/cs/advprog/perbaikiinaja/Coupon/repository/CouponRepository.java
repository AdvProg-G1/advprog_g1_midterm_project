package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.model.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String>{
}
