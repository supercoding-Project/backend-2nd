package com.github.secondproject.cart.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.cart.dto.AddCartDto;
import com.github.secondproject.cart.dto.UpdateCartDto;
import com.github.secondproject.cart.entity.CartEntity;
import com.github.secondproject.cart.entity.CartItemEntity;
import com.github.secondproject.cart.repository.CartItemRepository;
import com.github.secondproject.cart.repository.CartRepository;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final CartRepository cartRepository;
    public final CartItemRepository cartItemRepository;

    @Transactional
    public ResponseEntity<?> addProductToCart(UserEntity user, AddCartDto addCartDto) {
        // user 정보 DB에 존재하는지 체크
        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));
        // user 장바구니 가져오기 없으면 생성
        CartEntity cart = cartRepository.findByUser(userEntity)
                .orElseGet(() -> cartRepository.save(
                        CartEntity.builder()
                                .user(userEntity)
                                .build()));
        // 추가할 상품 조회
        ProductEntity product = productRepository.findById(addCartDto.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT, ErrorCode.NOT_FOUND_PRODUCT.getMessage()));

        // 장바구니에 동일 상품 있는지 확인
        Optional<CartItemEntity> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingItem.isPresent()){
            // 이미 존재한다면 수량 증가
            CartItemEntity cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + addCartDto.getQuantity());
        }
        else{
            CartItemEntity cartItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(addCartDto.getQuantity())
                    .build();
        }
        Map<String,String> response = new HashMap<>();
        response.put("message","상품이 장바구니에 추가 되었습니다.");
        return ResponseEntity.ok(response);

    }

    public void updateCart(UserEntity user, UpdateCartDto updateCartDto) {
    }

    public void deleteProductInCart(Long productId) {
    }
}
