package com.github.secondproject.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    CREATED("주문 생성"),
    PENDING("결제 대기"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소"),
    REFUNDED("환불 완료");

    private final String desc;


}
