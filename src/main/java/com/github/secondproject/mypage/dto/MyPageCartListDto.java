package com.github.secondproject.mypage.dto;

import com.github.secondproject.cart.entity.CartEntity;
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
