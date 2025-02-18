package com.github.secondproject.mypage.repository;

import com.github.secondproject.mypage.entity.MyPageUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageUserRepository extends JpaRepository<MyPageUserEntity, Integer> {
}
