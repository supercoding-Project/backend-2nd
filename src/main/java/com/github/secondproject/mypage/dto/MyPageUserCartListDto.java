package com.github.secondproject.mypage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageUserCartListDto {
    private Integer productId; //상품 Id
    private String productName; //상품 이름
    private Integer productPrice; // 상품 가격
    private Integer productQuantity; //상품 수량
}
