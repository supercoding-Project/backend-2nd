package com.github.secondproject.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.global.dto.MsgResponseDto;
import com.github.secondproject.global.service.AwsFileService;
import com.github.secondproject.product.dto.ProductDto;
import com.github.secondproject.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@ComponentScan
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController { // 쇼핑몰 물품 판매 기능

    private final ProductService productService;


    // 상품 등록
    @Operation(summary = "상품등록", description = "판매상품 등록 api 입니다.")
    @PostMapping
    public ResponseEntity<?> productRegister(@RequestParam("file") MultipartFile file
                , @RequestParam("product") String productDtoJson
                , @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        log.info("상품등록 api");
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto productDto = objectMapper.readValue(productDtoJson,ProductDto.class);
        productService.productRegister(productDto,file,customUserDetails.getUserEntity().getUserId());

        return ResponseEntity.ok(new MsgResponseDto("상품이 성공적으로 등록되었습니다.", HttpStatus.OK.value()));
    }

    // 유저 본인 판매내역 전체 조회
    @Operation(summary = "나의 판매내역", description = "나의 판매내역 api 입니다.")
    @GetMapping("/myproductlist")
    public ResponseEntity<?> myProductList(@AuthenticationPrincipal CustomUserDetails customUserDetails
                            , Pageable pageable) {
        log.info("[POST]: 나의 판매내역 검색 요청");
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt"))
        );
        return productService.myProductList(customUserDetails.getUserEntity().getUserId() ,sortedPageable);
    }

    // 유저 본인 판매내역 상세조회
    @Operation(summary = "나의 판매상품", description = "나의 판매상품 조회 api 입니다.")
    @GetMapping("/myproduct")
    public ResponseEntity<?> myProduct(
                    @Parameter(name = "product_id", description = "상품 ID", example = "1")
                    @PathVariable("product_id") Long productId,
                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[POST]: 나의 판매내역 검색 요청");
        return productService.myProduct(productId, customUserDetails.getUserEntity().getUserId());
    }


    // 유저 자신이 판매중인 상품 재고 수정
    @Operation(summary = "상품수정", description = "판매상품 수정 api 입니다.")
    @PutMapping
    public void productUpdate(@RequestParam("file") MultipartFile file
            , @RequestParam("product") String productDtoJson
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

    }

    // 판매중인 상품 삭제
    @Operation(summary = "상품삭제", description = "판매상품 삭제 api 입니다.")
    @DeleteMapping
    public void productDelete(@RequestParam Long productId
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    }

}
