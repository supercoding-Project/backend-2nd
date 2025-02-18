package com.github.secondproject.mypage.repository;

import com.github.secondproject.mypage.entity.MyPageUserEntity;
import com.github.secondproject.mypage.entity.MyPageUserOrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPageUserOrderHistoryRepository extends JpaRepository<MyPageUserOrderHistoryEntity, Integer> {
    List<MyPageUserOrderHistoryEntity> findByMyPageUserEntity(MyPageUserEntity myPageUserEntity);

    List<MyPageUserOrderHistoryEntity> findByMyPageUserEntity_MyPageUserId(Integer myPageUserEntityMyPageUserId);
}
