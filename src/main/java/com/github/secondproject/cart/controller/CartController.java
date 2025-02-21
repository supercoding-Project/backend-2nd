package com.github.secondproject.cart.controller;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.cart.dto.AddCartDto;
import com.github.secondproject.cart.dto.UpdateCartDto;
import com.github.secondproject.cart.service.CartService;
import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 상품 장바구니에 추가 하기
    @Operation(summary = "상품 장바구니에 담기")
    @PostMapping
    public ResponseEntity<?> addProductToCart(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AddCartDto addCartDto
            ) {
        if (customUserDetails == null) {
            throw new AppException(ErrorCode.NOT_AUTHORIZED_USER,ErrorCode.NOT_AUTHORIZED_USER.getMessage());
        }
        UserEntity user = customUserDetails.getUserEntity();
        return cartService.addProductToCart(user,addCartDto);
    }

    // 장바구니 수정하기
    @Operation(summary = "장바구니 수정")
    @PutMapping
    public ResponseEntity<?> updateCart(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody UpdateCartDto updateCartDto) {
        if (customUserDetails == null) {
            throw new AppException(ErrorCode.NOT_AUTHORIZED_USER,ErrorCode.NOT_AUTHORIZED_USER.getMessage());
        }
        UserEntity user = customUserDetails.getUserEntity();
        return cartService.updateCart(user,updateCartDto);
    }

    // 장바구니 상품 삭제
    @Operation(summary = "장바구니 상품 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteProductInCart(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(name = "product_id",description = "삭제할 상품 ID", example = "1")
            @RequestParam Long productId ){
        if (customUserDetails == null) {
            throw new AppException(ErrorCode.NOT_AUTHORIZED_USER,ErrorCode.NOT_AUTHORIZED_USER.getMessage());
        }
        UserEntity user = customUserDetails.getUserEntity();
        return cartService.deleteProductInCart(user,productId);
    }
}
