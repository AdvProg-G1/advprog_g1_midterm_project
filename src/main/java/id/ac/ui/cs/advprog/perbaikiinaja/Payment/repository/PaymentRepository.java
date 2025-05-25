package id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    boolean existsByPaymentNameIgnoreCase(String paymentName);
    boolean existsByPaymentNameIgnoreCaseAndPaymentIdNot(String newName, String paymentId);
}