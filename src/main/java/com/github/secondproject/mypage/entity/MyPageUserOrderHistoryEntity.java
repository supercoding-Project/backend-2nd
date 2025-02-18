package com.github.secondproject.mypage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mypage_user_order_history")
public class MyPageUserOrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private Integer orderHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mypage_user_id", nullable = false)
    private MyPageUserEntity myPageUserEntity;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus;
}
