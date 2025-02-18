package com.github.secondproject.product.entity;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.product.dto.ProductStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 ID 생성
    private Long id;

    @Column(nullable = false)
    private String title;  // 도서명

    @Column(nullable = false)
    private String author; // 저자

    @Column(nullable = false)
    private String publisher; // 출판사

    @Column(nullable = false)
    private Double originalPrice; // 정가

    @Column(nullable = false)
    private Double salePrice; // 판매가

    @Column(length = 1000)
    private String description; // 설명

    @Column(nullable = false)
    private Integer stockQuantity; // 수량

    @Column(nullable = false)
    private Date publishDate; // 출간일

    @Column(nullable = false)
    private Date startedAt; // 상품 등록일

    @Column(nullable = false)
    private Date terminatedAt; // 판매종료일

    @Column(nullable = false)
    private String imageUrl; // 도서 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status; // 도서 상태

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity; // 판매자
}
