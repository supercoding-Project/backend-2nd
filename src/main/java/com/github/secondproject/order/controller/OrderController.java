package com.github.secondproject.order.controller;

import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.order.dto.OrderRequestDTO;
import com.github.secondproject.order.dto.OrderResponse;
import com.github.secondproject.order.entity.OrderEntity;
import com.github.secondproject.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문하기 api")
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder (@RequestBody OrderRequestDTO orderRequestDTO,
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer userId = customUserDetails.getUserEntity().getUserId();
        OrderEntity order = orderService.createOrder(userId,orderRequestDTO.getCartItemIds());
        OrderResponse orderResponse = OrderResponse.from(order);
        return ResponseEntity.ok(orderResponse);
    }



}
