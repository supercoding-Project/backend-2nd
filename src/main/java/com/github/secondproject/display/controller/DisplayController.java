package com.github.secondproject.display.controller;

import com.github.secondproject.display.dto.ProductDisplayDto;
import com.github.secondproject.display.service.DisplayService;
import com.github.secondproject.product.entity.ProductEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<ProductDisplayDto>> getAllProduct(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("startedAt"))
        );
        return ResponseEntity.ok(displayService.getAllProducts(sortedPageable));
    }

    // 물품 상세 조회
    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductDetail(
            @Parameter(name = "product_id", description = "상품 ID", example = "1")
            @PathVariable("product_id") Long productId ) {

        ProductDisplayDto response = displayService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    //가격 순 정렬
    @Operation(summary = "가격 순서 정렬")
    @GetMapping("/sort")
    public ResponseEntity<Page<ProductDisplayDto>> sortByPrice(
            @Parameter(name = "by" ,description = "정렬 순서 (0:내림차순, 1: 오름차순)", example = "0")
            @RequestParam Integer by, Pageable pageable) {

        Sort.Order order = by == 0 ? Sort.Order.desc("salePrice") : Sort.Order.asc("salePrice");
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(order)
        );
        return ResponseEntity.ok(displayService.getAllProducts(sortedPageable));
    }

    // 도서상태 필터링
    @Operation(summary = "도서 상태 별로 조회하기")
    @GetMapping("/status/{state}")
    public ResponseEntity<Page<ProductDisplayDto>> getProductByStatus(
            @Parameter(name = "state", description = "도서 상태 별 조회(최상:0,상급:1,중금:2)", example = "0")
            @PathVariable Integer state, Pageable pageable) {
        return ResponseEntity.ok(displayService.getProductsByStatus(state,pageable));
    }

}
