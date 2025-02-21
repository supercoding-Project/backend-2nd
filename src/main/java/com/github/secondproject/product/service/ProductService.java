package com.github.secondproject.product.service;


import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.entity.UserStatus;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.global.service.AwsFileService;
import com.github.secondproject.product.dto.ProductDto;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final UserRepository userRepository;


    private final ProductRepository productRepository;

    private UserEntity findUserById(Long userId) {
        // 사용자 id가 존재하지않을 경우 error
        return userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.CHECK_USER_ID,ErrorCode.CHECK_USER_ID.getMessage()));

    }


    // 상품 등록
    public void productRegister(ProductDto productDto , Long userId) throws IOException {

        UserEntity userEntity = findUserById(userId);

        Date date = LocalDateTime.now().toDate();
        java.time.LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        try{
            ProductEntity productEntity = ProductEntity.builder()
                    .title(productDto.getTitle())
                    .author(productDto.getAuthor())
                    .publisher(productDto.getPublisher())
                    .originalPrice(productDto.getOriginalPrice())
                    .salePrice(productDto.getSalePrice())
                    .description(productDto.getDescription())
                    .stockQuantity(productDto.getStockQuantity())
                    .publishDate(productDto.getPublishDate())
                    .startedAt(date)
                    .terminatedAt(productDto.getTerminatedAt())
                    .imageUrl(productDto.getImageUrl())
                    .productStatus(productDto.getStatus())
                    .userEntity(userEntity)
                    .createAt(localDateTime)
                    .build();
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 본인 판매내역 전체 조회
    public void myProductList(Long userId) {

    }

    // 유저 본인 판매내역 상세조회
    public void myProduct(Long productId,Long userId) {

    }


    // 유저 자신이 판매중인 상품 재고 수정
    public void productUpdate(MultipartFile file, ProductDto productDto, Long userId) {

    }

    // 판매중인 상품 삭제
    public void productDelete(Long productId,Long userId) {
    }
}
