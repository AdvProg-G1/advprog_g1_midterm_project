package id.ac.ui.cs.advprog.perbaikiinaja.Payment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentModelTest {

    private List<Payment> payments;

    @BeforeEach
    void setUp() {
        this.payments = new ArrayList<>();
        Payment payment1 = new Payment();
        payment1.setPaymentId("id-01");
        payment1.setPaymentName("GoPay");
        payment1.setPaymentBankNumber("124567890");

        Payment payment2 = new Payment();
        payment2.setPaymentId("id-02");
        payment2.setPaymentName("OVO");
        payment2.setPaymentBankNumber("070707070");

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
    void testGetPaymentPaymentBankNumber() {
        assertEquals("124567890", this.payments.get(0).getPaymentBankNumber());
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutNameAndPaymentBankNumber() {
        Payment payment = new Payment();
        payment.setPaymentId("id-03");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentBankNumber(null);
        });
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutName() {
        Payment payment = new Payment();
        payment.setPaymentId("id-04");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentBankNumber("1234567890");
            payment.setPaymentName(null);
        });
    }

    // unhappy
    @Test
    void testCreatePaymentWithoutPaymentBankNumber() {
        Payment payment = new Payment();
        payment.setPaymentId("id-05");

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName("MasterCard");
            payment.setPaymentBankNumber("");
        });
    }

    // happy
    @Test
    void testCreateValidPaymentMethod() {
        Payment payment = new Payment();
        payment.setPaymentId("id-06");

        assertDoesNotThrow(() -> {
            payment.setPaymentName("MasterCard");
            payment.setPaymentBankNumber("222333444");
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
    void testUpdatePaymentMethodPaymentBankNumber() {
        Payment payment = this.payments.get(0);
        payment.setPaymentBankNumber("1111111111");

        assertEquals("1111111111", payment.getPaymentBankNumber());
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
    void testUpdatePaymentMethodPaymentBankNumberToEmpty() {
        Payment payment = this.payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentBankNumber("");
        });
    }

    // unhappy
    @Test
    void testUpdatePaymentMethodNameAndPaymentBankNumberToEmpty() {
        Payment payment = this.payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentName("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentBankNumber("");
        });
    }

    // happy
    @Test
    void testUpdatePaymentMethodNameAndPaymentBankNumber() {
        Payment payment = this.payments.get(0);
        payment.setPaymentName("BCA");
        payment.setPaymentBankNumber("1122334455");

        assertEquals("BCA", payment.getPaymentName());
        assertEquals("1122334455", payment.getPaymentBankNumber());
    }

    @Test
    void testConstructorShouldInitializeFieldsCorrectly() {
        String name = "Bank Transfer";
        String bankNumber = "1234567890";

        Payment payment = new Payment(name, bankNumber);

        assertNotNull(payment.getPaymentId(), "paymentId should not be null");
        assertEquals(name, payment.getPaymentName(), "paymentName should match input");
        assertEquals(bankNumber, payment.getPaymentBankNumber(), "paymentBankNumber should match input");
    }
}