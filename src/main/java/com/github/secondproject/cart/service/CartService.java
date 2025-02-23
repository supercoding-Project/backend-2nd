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
import org.apache.catalina.User;
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
        ProductEntity product = productRepository.findById(addCartDto.getProductId().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT, ErrorCode.NOT_FOUND_PRODUCT.getMessage()));

        // 장바구니에 동일 상품 있는지 확인
        cartItemRepository.findByCartAndProduct(cart,product)
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity()+addCartDto.getQuantity()),
                        () -> cartItemRepository.save(
                                CartItemEntity.builder()
                                        .cart(cart)
                                        .product(product)
                                        .quantity(addCartDto.getQuantity())
                                .build())
                );

        Map<String,String> response = new HashMap<>();
        response.put("message","상품이 장바구니에 추가 되었습니다.");
        return ResponseEntity.ok(response);

    }
    @Transactional
    public ResponseEntity<?> updateCart(UserEntity user, UpdateCartDto updateCartDto) {
        // user 정보 DB에 존재하는지 체크
        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        // cart 정보 가져오기
        CartEntity cart = cartRepository.findByUser(userEntity).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND, ErrorCode.CART_NOT_FOUND.getMessage()));

        // update 할 item 가져오기
        ProductEntity product = productRepository.findById(updateCartDto.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT, ErrorCode.NOT_FOUND_PRODUCT.getMessage()));

        // cartItem 가져오기
        CartItemEntity cartItem = cartItemRepository.findByCartAndProduct(cart,product)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_SELECTED, ErrorCode.CART_ITEM_NOT_SELECTED.getMessage()));

        // 수량 변경하기
        try {
            cartItem.setQuantity(updateCartDto.getQuantity());
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.NOT_ACCEPTABLE_CART, ErrorCode.NOT_ACCEPTABLE_CART.getMessage());
        }


        Map<String,String> response = new HashMap<>();
        response.put("message","상품의 주문 수량이 변경 되었습니다.");
        return ResponseEntity.ok(response);
    }
    @Transactional
    public ResponseEntity<?> deleteProductInCart(UserEntity user, Long productId) {
        // user 정보 DB에 존재하는지 체크
        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER, ErrorCode.NOT_FOUND_USER.getMessage()));

        // cart 정보 가져오기
        CartEntity cart = cartRepository.findByUser(userEntity).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND, ErrorCode.CART_NOT_FOUND.getMessage()));

        // update 할 item 가져오기
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT, ErrorCode.NOT_FOUND_PRODUCT.getMessage()));

        // cartItem 가져오기
        CartItemEntity cartItem = cartItemRepository.findByCartAndProduct(cart,product)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_SELECTED, ErrorCode.CART_ITEM_NOT_SELECTED.getMessage()));

        cartItemRepository.delete(cartItem);

        Map<String,String> response = new HashMap<>();
        response.put("message","상품이 장바구니에서 삭제 되었습니다.");
        return ResponseEntity.ok(response);
    }
}
