package com.github.secondproject.product.repository;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findByStockQuantityGreaterThan(Integer stockQuantityIsGreaterThan, Pageable pageable);
}
