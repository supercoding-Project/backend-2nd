package com.github.secondproject.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PENDING("결제 대기"),
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소"),
    REFUNDED("환불 완료");

    private final String desc;

}
