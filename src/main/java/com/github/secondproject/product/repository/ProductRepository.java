package com.github.secondproject.product.repository;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.product.entity.ProductEntity;
import com.github.secondproject.product.entity.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findByStockQuantityGreaterThan(Integer stockQuantityIsGreaterThan, Pageable pageable);

    Page<ProductEntity> findByProductStatus(ProductStatus productStatus, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.productId = :id")
    Optional<ProductEntity> findByIdForUpdate(@Param("id") Long id);

}
