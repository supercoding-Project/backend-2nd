package com.github.secondproject.mypage.controller;

import com.github.secondproject.mypage.dto.MyPageUserCartListDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.dto.MyPageUserOrderHistoryDto;
import com.github.secondproject.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<MyPageUserDto> myPage(
            @PathVariable("userId") Integer userId) {
        log.info("[GET]: 유저 정보 조회");

        MyPageUserDto myPageUserDto = myPageService.getMyPageUserDto(userId);
        if (myPageUserDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }

        if (myPageUserDto.getProfileImageUrl() == null || myPageUserDto.getProfileImageUrl().isEmpty()) {
            myPageUserDto.setProfileImageUrl("Images/defaultImage.png"); // 프로필 없으면 기본 이미지
        }

        return ResponseEntity.ok(myPageUserDto);
    }

    @Operation(summary = "유저 장바구니 물품 리스트 조회")
    @GetMapping("/{userId}/cart")
    public ResponseEntity<List<MyPageUserCartListDto>> myPageCartList(
            @PathVariable("userId") Integer userId) {
        log.info("[GET]: 유저의 장바구니 물품 리스트 조회");
        List<MyPageUserCartListDto> cartList = myPageService.getMyPageUserCartList(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니에 물품이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(cartList);
    }

    @Operation(summary = "구매했던 물품 조회")
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<MyPageUserOrderHistoryDto>> myPageOrderHistory(
            @PathVariable("userId") Integer userId){
        log.info("[GET]: 구매 목록 조회");
        List<MyPageUserOrderHistoryDto> orderHistoryDto = myPageService.getMyPageUserOrderHistory(userId);
        if (orderHistoryDto == null || orderHistoryDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "구매 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(orderHistoryDto);
    }
}
