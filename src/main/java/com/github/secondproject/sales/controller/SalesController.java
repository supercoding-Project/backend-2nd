package com.github.secondproject.sales.controller;

import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.product.dto.ProductDto;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalesController { // 쇼핑몰 물품 판매 기능

    // 상품 등록
    @Operation(summary = "상품등록", description = "판매상품 등록 api 입니다.")
    @PostMapping
    public void salesRegister(@RequestParam("file") MultipartFile file
            , @RequestParam("product") String productDtoJson
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {

    }

    // 유저 본인 판매내역 전체 조회
    @Operation(summary = "나의 판매내역", description = "나의 판매내역 api 입니다.")
    @GetMapping("/mysales")
    public void mySalesList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

    }

    // 유저 본인 판매내역 상세조회
    @Operation(summary = "나의 판매상품", description = "나의 판매상품 조회 api 입니다.")
    @GetMapping("/mysale")
    public void mySales(@RequestParam Long postId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

    }


    // 유저 자신이 판매중인 상품 재고 수정
    @Operation(summary = "상품수정", description = "판매상품 수정 api 입니다.")
    @PutMapping
    public void salesUpdate(@RequestParam("file") MultipartFile file
            , @RequestParam("product") String productDtoJson
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

    }

    // 판매중인 상품 삭제
    @Operation(summary = "상품삭제", description = "판매상품 삭제 api 입니다.")
    @DeleteMapping
    public void salesMyList(@RequestParam Long postId
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

    }

}
