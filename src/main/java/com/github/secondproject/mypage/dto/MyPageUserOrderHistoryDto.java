package com.github.secondproject.mypage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageUserOrderHistoryDto {
    private Integer productId; //상품 Id
    private String productName; //상품 이름
    private Integer productPrice; // 상ㅠ품 가격
    private Integer productQuantity; //상품 수량
    private LocalDateTime orderDateTime; //주문한 날짜
    private String orderStatus; //주문 상태
}
