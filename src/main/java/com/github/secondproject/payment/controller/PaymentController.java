package com.github.secondproject.payment.controller;

import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.payment.dto.PaymentResponse;
import com.github.secondproject.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 요청
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<PaymentResponse> processPayment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          @PathVariable Long orderId) {
        Long userId = customUserDetails.getUserEntity().getUserId();
        PaymentResponse response = paymentService.processPayment(userId,orderId);
        return ResponseEntity.ok(response);
    }


    // 결제 내역 조회


}
