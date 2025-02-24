package com.github.secondproject.display.service;

import com.github.secondproject.display.dto.ProductDisplayDto;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.entity.ProductStatus;
import com.github.secondproject.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DisplayService {
    private final ProductRepository productRepository;

    public Page<ProductDisplayDto> getAllProducts(Pageable pageable) {
        //재고가 있는 물품들만 보여준다
        Page<ProductEntity> productPage = productRepository.findByStockQuantityGreaterThan(0,pageable);
        List<ProductDisplayDto> dtos = productPage.stream()
                .map(this::toProductDisplayDto)
                .toList();
        return new PageImpl<>(dtos,pageable,productPage.getTotalElements());
    }

    public ProductDisplayDto getProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT,ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
        return toProductDisplayDto(product);
    }

    public Page<ProductDisplayDto> getProductsByStatus(Integer state, Pageable pageable) {
        try{
            ProductStatus status = ProductStatus.values()[state];
            Page<ProductEntity> productPage = productRepository.findByProductStatus(status,pageable);
            List<ProductDisplayDto> dtos = productPage.stream()
                    .map(this::toProductDisplayDto)
                    .toList();
            return new PageImpl<>(dtos,pageable,productPage.getTotalElements());
        } catch (Exception e){
            throw new AppException(ErrorCode.NOT_FOUND_STATUS,ErrorCode.NOT_FOUND_STATUS.getMessage());
        }
    }

    private ProductDisplayDto toProductDisplayDto(ProductEntity product) {
        return ProductDisplayDto.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .author(product.getAuthor())
                .publisher(product.getPublisher())
                .originalPrice(product.getOriginalPrice())
                .salePrice(product.getSalePrice())
                .description(product.getDescription())
                .stockQuantity(product.getStockQuantity())
                .publishDate(product.getPublishDate())
                .startedAt(product.getStartedAt())
                .terminatedAt(product.getTerminatedAt())
                .imageUrl(product.getImageUrl())
                .status(product.getProductStatus())
                .createdAt(product.getCreatedAt())
                .userId(product.getUserEntity().getUserId())
                .build();
    }
}
