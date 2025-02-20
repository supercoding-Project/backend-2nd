package com.github.secondproject.mypage.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageCartListDto {
    private List<CartEntity> cartList;
}
