package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.dto.CouponResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@Import(TestSecurityConfig.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;
    
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCreateCouponReturns201() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setType("fixed");
        request.setDiscountValue(10000.0);
        request.setMaxUsage(5);

        CouponResponse response = new CouponResponse(
                "abc123", "CPN-TEST01", "fixed", 10000.0, 5, 0
        );

        when(couponService.createCoupon(request)).thenReturn(response);
        System.out.println(response);
        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("CPN-TEST01"))
                .andExpect(jsonPath("$.discountValue").value(10000.0))
                .andExpect(jsonPath("$.maxUsage").value(5));
    }

    @Test
    void testGetAllCouponsReturns200() throws Exception {
        List<CouponResponse> responses = List.of(
            new CouponResponse("1", "CPN-1", "fixed", 10000, 5, 0),
            new CouponResponse("2", "CPN-2", "percentage", 10, 3, 1)
        );

        when(couponService.getAllCoupons()).thenReturn(responses);

        mockMvc.perform(get("/api/coupons"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].code").value("CPN-1"))
               .andExpect(jsonPath("$[1].type").value("percentage"));
    }

    @Test
    void testGetCouponByIdReturns200() throws Exception {
        CouponResponse response = new CouponResponse("1", "CPN-GET", "fixed", 15000, 2, 0);

        when(couponService.getCouponById("1")).thenReturn(response);

        mockMvc.perform(get("/api/coupons/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("CPN-GET"))
               .andExpect(jsonPath("$.discountValue").value(15000));
    }

    @Test
    void testUpdateCouponReturns200() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setDiscountValue(20000.0);
        request.setMaxUsage(10);
        request.setType("fixed");

        CouponResponse updated = new CouponResponse("1", "CPN-UPDATE", "fixed", 20000, 10, 0);

        when(couponService.updateCoupon(eq("1"), any(CouponRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/coupons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("CPN-UPDATE"))
               .andExpect(jsonPath("$.discountValue").value(20000));
    }

    @Test
    void testDeleteCouponReturns204() throws Exception {
        mockMvc.perform(delete("/api/coupons/1"))
               .andExpect(status().isNoContent());

        verify(couponService, times(1)).deleteCoupon("1");
    }
}
