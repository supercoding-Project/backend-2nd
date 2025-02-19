package com.github.secondproject.display.controller;

import com.github.secondproject.display.service.DisplayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/display")
@RequiredArgsConstructor
public class DisplayController {
    private final DisplayService displayService;
    // 전체 물품 조회
    @Operation(summary = "전체 상품 조회")
    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct() {
        List<ProductEntity> response = displayService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    // 물품 상세 조회
    @Operation(summary = "상품 상세 조회")
    @PostMapping("/{product_id}")
    public ResponseEntity<?> getProductDetail(
            @Parameter(name = "product_id", description = "상품 ID", example = "1")
            @PathVariable("product_id") Long productId ) {
        ProductEntity response = displayService.getProduct(productId);

        return ResponseEntity.ok(response);
    }

    //TODO : 가격 순 정렬
    //TODO : 가격 범위 검색
    //TODO : 도서상태 필터링
}
