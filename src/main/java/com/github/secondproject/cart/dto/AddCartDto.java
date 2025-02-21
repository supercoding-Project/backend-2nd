package com.github.secondproject.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddCartDto {
    private  Integer productId;
    private  Integer quantity;
}
