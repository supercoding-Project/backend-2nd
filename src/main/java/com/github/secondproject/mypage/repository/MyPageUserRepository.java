package com.github.secondproject.mypage.repository;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.mypage.entity.MyPageUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyPageUserRepository extends JpaRepository<MyPageUserEntity, Long > {
    Optional<MyPageUserEntity> findByUserEntity_UserId(Long userEntityUserId);
}
