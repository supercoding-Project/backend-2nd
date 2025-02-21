package com.github.secondproject.display.service;

import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.entity.ProductStatus;
import com.github.secondproject.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DisplayService {
    private final ProductRepository productRepository;

    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        //재고가 있는 물품들만 보여준다
        return productRepository.findByStockQuantityGreaterThan(0,pageable);
    }

    public ProductEntity getProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PRODUCT,ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
    }

    public Page<ProductEntity> getProductsByStatus(Integer state, Pageable pageable) {
        try{
            ProductStatus status = ProductStatus.values()[state];
            return productRepository.findByProductStatus(status,pageable);
        } catch (Exception e){
            throw new AppException(ErrorCode.NOT_FOUND_STATUS,ErrorCode.NOT_FOUND_STATUS.getMessage());
        }

    }
}
