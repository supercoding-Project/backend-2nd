package com.github.secondproject.product.dto;

import java.util.Date;

public class ProductDto {
    private String title;  // 도서명
    private String author; // 저자
    private String publisher; // 출판사
    private Double originalPrice; // 정가
    private Double salePrice; // 판매가
    private String description; // 설명
    private Integer stockQuantity; // 수량
    private Date publishDate; // 출간일
    private String imageUrl; // 도서 이미지 URL
    private ProductStatus status; // 도서 상태 (최상, 상, 중, 하)

    private Long user_id; // 판매자 ID (
}
