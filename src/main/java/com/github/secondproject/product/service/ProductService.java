package com.github.secondproject.product.service;


import com.amazonaws.services.s3.AmazonS3Client;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AwsFileService awsFileService;

    private final ProductRepository productRepository;

    ///
    private UserEntity findUserById(Long userId) {
        // 사용자 id가 존재하지않을 경우 error
        return userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.CHECK_USER_ID,ErrorCode.CHECK_USER_ID.getMessage()));

    }

    private ProductEntity findProductById(Long productId) {
        // 상품 id가 존재하지않을 경우 error
        return productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.CHECK_USER_ID,ErrorCode.CHECK_USER_ID.getMessage()));

    }

    // 상품 등록
    @Transactional
    public void productRegister(ProductDto productDto ,MultipartFile file, Long userId) throws IOException {

        UserEntity userEntity = findUserById(userId);
        /*
            예외처리 추가 필요
         */
        String imageUrl = awsFileService.savePhoto(file, 1L);

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
                    .imageUrl(imageUrl)
                    .productStatus(productDto.getStatus())
                    .userEntity(userEntity)
                    .createdAt(localDateTime)
                    .build();
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 유저 본인 판매내역 전체 조회
    @Transactional
    public ResponseEntity<?> myProductList(Long userId, Pageable pageable) {
        Page<ProductEntity> productsPage  = productRepository.findByUserEntity_UserId(userId, pageable);

        List<ProductDto> productDtos = new ArrayList<>();
        try{
            for (ProductEntity productEntity : productsPage ) {
                ProductDto productDto = new ProductDto(
                        productEntity.getProductId(),
                        productEntity.getTitle(),
                        productEntity.getAuthor(),
                        productEntity.getPublisher(),
                        productEntity.getOriginalPrice(),
                        productEntity.getSalePrice(),
                        productEntity.getDescription(),
                        productEntity.getStockQuantity(),
                        productEntity.getPublishDate(),
                        productEntity.getTerminatedAt(),
                        productEntity.getProductStatus(),
                        productEntity.getUserEntity().getUsername(),
                        productEntity.getImageUrl()
                );
                productDtos.add(productDto);
            }
            return ResponseEntity.ok(productDtos);
        }catch (Exception e){
            throw new AppException(ErrorCode.NOT_ACCEPT,ErrorCode.NOT_ACCEPT.getMessage());
        }
    }

    // 유저 본인 판매내역 상세조회
    public ResponseEntity<?> myProduct(Long productId,Long userId) {
        ProductEntity productEntity = findProductById(productId);
        UserEntity userEntity = findUserById(productEntity.getUserEntity().getUserId());
        try {
            ProductDto productDto = new ProductDto(
                    productEntity.getProductId(),
                    productEntity.getTitle(),
                    productEntity.getAuthor(),
                    productEntity.getPublisher(),
                    productEntity.getOriginalPrice(),
                    productEntity.getSalePrice(),
                    productEntity.getDescription(),
                    productEntity.getStockQuantity(),
                    productEntity.getPublishDate(),
                    productEntity.getTerminatedAt(),
                    productEntity.getProductStatus(),
                    productEntity.getUserEntity().getUsername(),
                    productEntity.getImageUrl()
            );
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }catch (Exception e){
            throw new AppException(ErrorCode.NOT_ACCEPT,ErrorCode.NOT_ACCEPT.getMessage());
        }
    }


    // 유저 자신이 판매중인 상품 재고 수정
    public void productUpdate(MultipartFile file, ProductDto productDto, Long userId) {
        ProductEntity productEntity = findProductById((productDto.getProductId()));
        UserEntity userEntity = findUserById(userId);

        // 작성자와 사용자가 일치하지않는 경우 error
        if(!userEntity.getUserId().equals(productEntity.getUserEntity().getUserId())) {
            throw new AppException(ErrorCode.NOT_EQUAL_POST_USER,ErrorCode.NOT_EQUAL_POST_USER.getMessage());
        }

        try{
            productEntity.setProductEntity(productDto);
        }catch (Exception e){
            throw new AppException(ErrorCode.NOT_ACCEPT,ErrorCode.NOT_ACCEPT.getMessage());
        }
    }

    // 판매중인 상품 삭제
    public void productDelete(Long productId,Long userId) {
    }
}
