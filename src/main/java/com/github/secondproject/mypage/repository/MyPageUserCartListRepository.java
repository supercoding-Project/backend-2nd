package com.github.secondproject.mypage.repository;

import com.github.secondproject.mypage.entity.MyPageUserCartListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPageUserCartListRepository extends JpaRepository<MyPageUserCartListEntity, Integer> {
    List<MyPageUserCartListEntity> findByMyPageUserEntity_MyPageUserId(Integer userId);
}
