package com.github.secondproject.payment.entity;

import com.github.secondproject.OrderEntity;
import com.github.secondproject.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@DynamicInsert
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "paymentDateTime", nullable = false)
    private Timestamp PaymentDateTime;
}
