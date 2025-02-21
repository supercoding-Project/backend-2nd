package com.github.secondproject.cart.repository;

import com.github.secondproject.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {

    @Query("SELECT c FROM CartEntity c WHERE c.user.userId = :userId")
    Optional<CartEntity> findByUserId(@Param("userId") Long userId);

}
