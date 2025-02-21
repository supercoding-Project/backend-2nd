package com.github.secondproject.order.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.cart.entity.CartEntity;
import com.github.secondproject.cart.entity.CartItemEntity;
import com.github.secondproject.cart.repository.CartRepository;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.order.entity.OrderEntity;
import com.github.secondproject.order.entity.OrderItem;
import com.github.secondproject.order.entity.OrderStatus;
import com.github.secondproject.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Transactional
    public OrderEntity createOrder(Long userId, List<Integer> cartItemIds) {

        UserEntity user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,ErrorCode.NOT_FOUND_USER.getMessage()));

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND,ErrorCode.CART_NOT_FOUND.getMessage()));

        // cartItemIds로 장바구니 아이템 선택
        List<CartItemEntity> cartItems = cart.getCartItems().stream()
                .filter(item -> cartItemIds.contains(item.getCartItemId().intValue()))
                .collect(Collectors.toList());

        // 선택된 장바구니 아이템이 없으면 예외처리
        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_SELECTED,ErrorCode.CART_ITEM_NOT_SELECTED.getMessage());
        }

        // 주문 엔티티 생성
        OrderEntity order = new OrderEntity();
        order.setUser(user);
//        order.setTotalPrice(calculateTotalPrice(cartItems));
//        order.setOrderStatus(OrderStatus.PENDING);
//
//        // CartItem -> OrderItem 매핑
//        List<OrderItem> orderItems = cartItems.stream()
//                .map(cartItem -> OrderItem.createOrderItem(
//                        order,
//                        cartItem.getProduct(),
//                        cartItem.getQuantity(),
//                        BigDecimal.valueOf(cartItem.getProduct().getSalePrice())
//                ))
//                .collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//        orderRepository.save(order);

        return order;

    }
//
//    private BigDecimal calculateTotalPrice(List<CartItemEntity> cartItems) {
//        return cartItems.stream()
//                .map(item -> BigDecimal.valueOf(item.getProduct().getSalePrice())
//                        .multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}

