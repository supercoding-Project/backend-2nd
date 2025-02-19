package com.github.secondproject.mypage.dto;

import com.github.secondproject.product.entity.ProductEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageCartListDto {
    private List<ProductEntity> cartList;
}
