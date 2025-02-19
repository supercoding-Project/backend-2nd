package com.github.secondproject.order.dto;

import com.github.secondproject.order.entity.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Integer orderId;
    private Timestamp orderDate;
    private String orderStatus;
    private BigDecimal totalPrice;

    public static OrderResponse from(OrderEntity order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus().name())
                .totalPrice(order.getTotalPrice())
                .build();
    }

}
