package id.ac.ui.cs.advprog.perbaikiinaja.Payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private List<Payment> payments;

    @BeforeEach
    void setUp() {
        this.payments = new ArrayList<>();
        Payment payment1 = new Payment();
        payment1.setPaymentId("id-01");
        payment1.setPaymentName("GoPay");
        payment1.setAccountNumber("124567890");

        Payment payment2 = new Payment();
        payment2.setPaymentId("id-02");
        payment2.setPaymentName("OVO");
        payment2.setAccountNumber("070707070");

        this.payments.add(payment1);
        this.payments.add(payment2);
    }

    @Test
    void testGetPaymentId() {
        assertEquals("id-01", this.payments.get(0).getPaymentId());
    }

    @Test
    void testGetPaymentName() {
        assertEquals("GoPay", this.payments.get(0).getPaymentName());
    }

    @Test
    void testGetPaymentAccountNumber() {
        assertEquals("124567890", this.payments.get(0).getAccountNumber());
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutNameAndAccountNumber() {
        Payment payment = new Payment();
        payment.setPaymentId("id-03");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setAccountNumber(null);
        });
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutName() {
        Payment payment = new Payment();
        payment.setPaymentId("id-04");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setAccountNumber("1234567890");
            payment.setPaymentName(null);
        });
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutAccountNumber() {
        Payment payment = new Payment();
        payment.setPaymentId("id-05");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName("MasterCard");
            payment.setAccountNumber("");
        });
    }

    // happy
    @Test
    void testCreateValidPaymentMethod() {
        Payment payment = new Payment();
        payment.setPaymentId("id-06");

        assertDoesNotThrow(() -> {
            payment.setPaymentName("MasterCard");
            payment.setAccountNumber("222333444");
        });
    }

    // happy
    @Test
    void testUpdatePaymentMethodName() {
        Payment payment = this.payments.get(0);
        payment.setPaymentName("Dana");

        assertEquals("Dana", payment.getPaymentName());
    }

    // happy
    @Test
    void testUpdatePaymentMethodAccountNumber() {
        Payment payment = this.payments.get(0);
        payment.setAccountNumber("1111111111");

        assertEquals("1111111111", payment.getAccountNumber());
    }

    // unhappy
    @Test
    void testUpdatePaymentMethodNameToEmpty() {
        Payment payment = this.payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName("");
        });
    }

    // unhappy
    @Test
    void testUpdatePaymentMethodAccountNumberToEmpty() {
        Payment payment = this.payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setAccountNumber("");
        });
    }

    // unhappy
    @Test
    void testUpdatePaymentMethodNameAndAccountNumberToEmpty() {
        Payment payment = this.payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setAccountNumber("");
        });
    }

    // happy
    @Test
    void testUpdatePaymentMethodNameAndAccountNumber() {
        Payment payment = this.payments.get(0);
        payment.setPaymentName("BCA");
        payment.setAccountNumber("1122334455");

        assertEquals("BCA", payment.getPaymentName());
        assertEquals("1122334455", payment.getAccountNumber());
    }
}
