package com.github.secondproject.product.dto;

import com.github.secondproject.product.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String title;  // 도서명
    private String author; // 저자
    private String publisher; // 출판사
    private Double originalPrice; // 정가
    private Double salePrice; // 판매가
    private String description; // 설명
    private Integer stockQuantity; // 수량
    private Date publishDate; // 출간일
    private ProductStatus status; // 도서 상태

    private String username; // 판매자 닉네임
}
