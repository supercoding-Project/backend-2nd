package com.github.secondproject.auth.repository;

import com.github.secondproject.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    void deleteByRefreshToken(String refreshToken);
}
