package com.github.secondproject.display.dto;

import com.github.secondproject.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
public class ProductDisplayDto {
    private Long productId;
    private String title;
    private String author;
    private String publisher;
    private Double originalPrice;
    private Double salePrice;
    private String description;
    private Integer stockQuantity;
    private Date publishDate;
    private Date startedAt;
    private Date terminatedAt;
    private String imageUrl;
    private ProductStatus status;
    private LocalDateTime createdAt;

    private Long userId; // 판매자 정보

}
