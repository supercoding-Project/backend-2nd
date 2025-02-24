package com.github.secondproject.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCartDto {
    private  Long productId;
    private  Integer quantity;
}
