package com.github.secondproject.mypage.service;

import com.github.secondproject.mypage.dto.MyPageUserCartListDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.dto.MyPageUserOrderHistoryDto;
import com.github.secondproject.mypage.entity.MyPageUserCartListEntity;
import com.github.secondproject.mypage.entity.MyPageUserEntity;
import com.github.secondproject.mypage.entity.MyPageUserOrderHistoryEntity;
import com.github.secondproject.mypage.repository.MyPageUserCartListRepository;
import com.github.secondproject.mypage.repository.MyPageUserOrderHistoryRepository;
import com.github.secondproject.mypage.repository.MyPageUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageUserRepository myPageUserRepository;
    private final MyPageUserCartListRepository myPageUserCartListRepository;
    private final MyPageUserOrderHistoryRepository myPageUserOrderHistoryRepository;

    //유저 정보 조회
    @Transactional
    public MyPageUserDto getMyPageUserDto(Integer userId) {
        MyPageUserEntity myPageUserEntity = myPageUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        return MyPageUserDto.builder()
                .userId(myPageUserEntity.getMyPageUserId())
                .email(myPageUserEntity.getEmail())
                .aboutMe(myPageUserEntity.getAboutMe())
                .address(myPageUserEntity.getAddress())
                .phoneNumber(myPageUserEntity.getPhoneNumber())
                .build();

    }

    //유저 장바구니 물품 리스트 조회
    @Transactional(readOnly = true)
    public List<MyPageUserCartListDto> getMyPageUserCartList(Integer userId) {
        List<MyPageUserCartListEntity> cartListEntities = myPageUserCartListRepository.findByMyPageUserEntity_MyPageUserId(userId);

        if (cartListEntities.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        return cartListEntities.stream()
                .map(myPageUserCartListEntity -> MyPageUserCartListDto.builder()
                        .productId(myPageUserCartListEntity.getProductId())
                        .productName(myPageUserCartListEntity.getProductName())
                        .productPrice(myPageUserCartListEntity.getProductPrice())
                        .productQuantity(myPageUserCartListEntity.getProductQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    // 유저의 구매 물품 내역 조회
    @Transactional(readOnly = true)
    public List<MyPageUserOrderHistoryDto> getMyPageUserOrderHistory(Integer userId) {
        List<MyPageUserOrderHistoryEntity> orderHistoryEntities = myPageUserOrderHistoryRepository.findByMyPageUserEntity_MyPageUserId(userId);

        if (orderHistoryEntities.isEmpty()) {
            throw new RuntimeException("구매 내역이 존재하지 않습니다.");
        }

        return orderHistoryEntities.stream()
                .map(orderHistoryEntity -> MyPageUserOrderHistoryDto.builder()
                        .productId(orderHistoryEntity.getProductId())
                        .productName(orderHistoryEntity.getProductName())
                        .productPrice(orderHistoryEntity.getProductPrice())
                        .productQuantity(orderHistoryEntity.getProductQuantity())
                        .orderDateTime(orderHistoryEntity.getOrderDateTime())
                        .orderStatus(orderHistoryEntity.getOrderStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
