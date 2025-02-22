package com.github.secondproject.payment.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.order.entity.OrderEntity;
import com.github.secondproject.order.entity.OrderItem;
import com.github.secondproject.order.entity.OrderStatus;
import com.github.secondproject.order.repository.OrderRepository;
import com.github.secondproject.payment.dto.PaymentResponse;
import com.github.secondproject.payment.entity.PaymentEntity;
import com.github.secondproject.payment.entity.PaymentStatus;
import com.github.secondproject.payment.repository.PaymentRepository;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Transactional(rollbackFor = {AppException.class, Exception.class})
    public PaymentResponse processPayment(Long userId, Long orderId) {

        // 주문 검증
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND,ErrorCode.ORDER_NOT_FOUND.getMessage()));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_USER,ErrorCode.UNAUTHORIZED_USER.getMessage()); // 오더유저 = 해당 유저 일치 확인
        }

        // 예산 검증
        UserEntity user = order.getUser();
        Integer totalPrice = order.getTotalPrice().intValue();

        if (user.getBudget() < totalPrice) {
            saveFailedPayment(order,totalPrice,"예산 부족으로 결제 실패");
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS,
                    "예산 부족: 현재 예산 " + user.getBudget() + ", 결제 금액 " + totalPrice);
        }

        // 재고 검증 및 차감
        for (OrderItem orderItem : order.getOrderItems()) {
            ProductEntity product = productRepository.findByIdForUpdate(orderItem.getProduct().getProductId())  // 비관적 락 사용해서 재고 검증
                    .orElseThrow(() -> new AppException(ErrorCode.STOCK_VALIDATION_FAILED,ErrorCode.STOCK_VALIDATION_FAILED.getMessage()));

            if(product.getStockQuantity() < orderItem.getQuantity()) {
                saveFailedPayment(order,totalPrice,product.getTitle() + " 의 재고 부족으로 결제 실패");
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK, product.getTitle() + " 의 재고가 부족합니다.");
            }
            product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
        }

        // 결제처리 및 정보 저장
        try {
            user.setBudget(user.getBudget() - totalPrice);
            order.setOrderStatus(OrderStatus.PAID);
            orderRepository.save(order);

            PaymentEntity payment = PaymentEntity.builder()
                    .order(order)
                    .user(user)
                    .totalPrice(totalPrice)
                    .status(PaymentStatus.COMPLETED)
                    .paymentDateTime(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            paymentRepository.save(payment);

            return new PaymentResponse(payment.getPaymentId(),payment.getStatus(),payment.getTotalPrice(),"결제가 완료되었습니다.");
        }catch (Exception e) {
            saveFailedPayment(order,totalPrice,"결제 처리 중 시스템 오류");
            throw new AppException(ErrorCode.PAYMENT_PROCESSING_ERROR,ErrorCode.PAYMENT_PROCESSING_ERROR.getMessage());
        }

    }
    // 결제 실패 내역 저장
    private void saveFailedPayment(OrderEntity order, Integer amount, String reason) {
        PaymentEntity failedPayment = PaymentEntity.builder()
                .order(order)
                .user(order.getUser())
                .totalPrice(amount)
                .status(PaymentStatus.FAILED)
                .paymentDateTime(Timestamp.valueOf(LocalDateTime.now()))
                .failReason(reason)
                .build();
        paymentRepository.save(failedPayment);
    }
}

