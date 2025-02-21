package com.github.secondproject.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.secondproject.product.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String title;  // 도서명
    private String author; // 저자
    private String publisher; // 출판사
    @JsonProperty("original_price")
    private Double originalPrice; // 정가
    @JsonProperty("sale_price")
    private Double salePrice; // 판매가
    private String description; // 설명
    @JsonProperty("stock_quantity")
    private Integer stockQuantity; // 수량
    @JsonProperty("publish_date")
    private Date publishDate; // 출간일
    @JsonProperty("terminated_at")
    private Date terminatedAt; // 판매종료일
    @JsonProperty("product_status")
    private ProductStatus status; // 도서 상태

    private String username; // 판매자 닉네임
    private String imageUrl;
}
