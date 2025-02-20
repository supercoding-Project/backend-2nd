package com.github.secondproject.mypage.controller;

import com.github.secondproject.global.config.auth.custom.CustomUserDetails;
import com.github.secondproject.mypage.dto.MyPageCartListDto;
import com.github.secondproject.mypage.dto.MyPageOrderHistoryDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "유저 정보 조회", description = "인증된 userId 를 기반으로 해당 유저의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<MyPageUserDto> myPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("userId") Long userId) {
        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다.");
        }
        if (!customUserDetails.getUserEntity().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 정보만 조회할 수 있습니다.");
        }
        log.info("[GET]: 유저 정보 조회");

        MyPageUserDto myPageUserDto = myPageService.getMyPageUserDto(userId);

        if (myPageUserDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 정보를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(myPageUserDto);
    }

    @Operation(summary = "유저 정보 수정", description = "인증된 userId를 기반으로 사용자의 정보를 불러와 사용자의 정보를 수정할 수 있습니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<MyPageUserDto> updateMyPageUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("userId") Long userId,
            @RequestBody MyPageUserDto myPageUserDto) {
        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다.");
        }
        if (!customUserDetails.getUserEntity().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 정보만 수정할 수 있습니다.");
        }
        log.info("[PUT]: 유저 정보 수정");

        // 수정 정보 업데이트
        myPageService.updateMyPage(customUserDetails.getUserEntity().getUserId(), myPageUserDto);

        // 수정된 정보 반환
        MyPageUserDto updatedMyPageUser = myPageService.getMyPageUserDto(userId);

        if (updatedMyPageUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 정보를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(updatedMyPageUser);
    }

    @Operation(summary = "유저 장바구니 물품 리스트 조회", description = "인증된 userId를 기반으로 장바구니의 물품 리스트를 조회합니다.")
    @GetMapping("/{userId}/cart")
    public ResponseEntity<List<MyPageCartListDto>> myPageCartList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("userId") Long userId) {
        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다.");
        }
        log.info("[GET]: 유저의 장바구니 물품 리스트 조회");

        List<MyPageCartListDto> cartList = myPageService.getMyPageCartList(userId);

        if (cartList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니에 상품이 없습니다.");
        }
        return ResponseEntity.ok(cartList);
    }

    @Operation(summary = "구매 내역 조회", description = "인증된 userId를 기반으로 사용자의 구매 내역을 조회합니다.")
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<MyPageOrderHistoryDto>> myPageOrderHistory(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("userId") Long userId){
        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다.");
        }
        log.info("[GET]: 구매 목록 조회");

        List<MyPageOrderHistoryDto> orderHistoryDto = myPageService.getMyPageOrderHistory(userId);

        if (orderHistoryDto == null || orderHistoryDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "구매 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(orderHistoryDto);
    }
}
