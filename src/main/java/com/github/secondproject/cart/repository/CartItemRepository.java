package com.github.secondproject.cart.repository;

import com.github.secondproject.cart.entity.CartEntity;
import com.github.secondproject.cart.entity.CartItemEntity;
import com.github.secondproject.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity,Long> {

    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
}
