package com.github.secondproject.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Auth 에러코드
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    BINDING_RESULT_ERROR(HttpStatus.BAD_REQUEST, "데이터 유효성에 문제가 있습니다."),
    CHECK_EMAIL_OR_PASSWORD(HttpStatus.NOT_FOUND, "이메일 또는 비밀번호가 올바르지 않습니다."),
    NOT_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다"),

    // 게시물 에러코드
    NOT_EQUAL_POST_USER(HttpStatus.BAD_REQUEST, "게시물 수정/삭제 권한이 없습니다."),
    CHECK_USER_ID(HttpStatus.NOT_FOUND, "작성자 정보가 유효하지않습니다."),
    CHECK_POST_ID(HttpStatus.NOT_FOUND, "게시물이 유효하지않습니다."),
    NOT_ACCEPT(HttpStatus.NOT_ACCEPTABLE, "해당 페이지에 오류가 발생했습니다."),
      
    // 상품 진열 에러코드
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    // MyPage 에러코드
    NOT_FOUND_USERINFO(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    DELETE_USERINFO(HttpStatus.NOT_FOUND,"삭제된 사용자 정보입니다."),
    NOT_FOUND_CART_LIST(HttpStatus.NOT_FOUND,"장바구니에 상품이 존재하지 않습니다."),
    NOT_FOUND_ORDER_HISTORY(HttpStatus.NOT_FOUND, "구매 내역이 존재하지 않습니다."),
    MY_PAGE_CART_ERROR(HttpStatus.BAD_REQUEST,"장바구니 조회 중 오류가 발생했습니다."),
    MY_PAGE_ORDER_ERROR(HttpStatus.BAD_REQUEST,"구매 내역 조회 중 오류가 발생했습니다."),


    // 주문 에러코드
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. 유저 정보를 확인해주세요."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND,"유저의 장바구니를 찾을 수 없습니다."),
    CART_ITEM_NOT_SELECTED(HttpStatus.NOT_FOUND,"장바구니에서 선택된 상품이 없습니다. 상품을 선택해주세요.")


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
