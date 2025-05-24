package id.ac.ui.cs.advprog.perbaikiinaja.Coupon.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CouponNotificationController {

    private final SimpMessagingTemplate messagingTemplate;


    public CouponNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyCouponListChanged() {
        messagingTemplate.convertAndSend("/topic/coupons", "refresh");
    }

}
