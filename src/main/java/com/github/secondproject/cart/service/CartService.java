package com.github.secondproject.cart.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.cart.dto.AddCartDto;
import com.github.secondproject.cart.dto.UpdateCartDto;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    public void addProductToCart(UserEntity user, AddCartDto addCartDto) {
    }

    public void updateCart(UserEntity user, UpdateCartDto updateCartDto) {
    }

    public void deleteProductInCart(Long productId) {
    }
}
