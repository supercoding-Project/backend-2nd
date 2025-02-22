package com.github.secondproject.product.entity;

import com.github.secondproject.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id",nullable = false)
    private Long productId;

    @Column(name="title",nullable = false)
    private String title;  // 도서명

    @Column(name="author",nullable = false)
    private String author; // 저자

    @Column(name="publisher",nullable = false)
    private String publisher; // 출판사

    @Column(name="original_price",nullable = false)
    private Double originalPrice;

    @Column(name="sale_price",nullable = false)
    private Double salePrice;

    @Column(name="description",length = 1000)
    private String description;

    @Column(name="stock_quantity",nullable = false)
    private Integer stockQuantity;

    @Column(name="publish_date",nullable = false)
    private Date publishDate;

    @Column(name="started_at",nullable = false)
    private Date startedAt;

    @Column(name="terminated_at",nullable = false)
    private Date terminatedAt;

    @Column(name="image_url",nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status" ,nullable = false)
    private ProductStatus productStatus;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity; // 판매자

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
