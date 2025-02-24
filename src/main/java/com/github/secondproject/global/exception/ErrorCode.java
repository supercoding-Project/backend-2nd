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
    NOT_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    VALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "유효한 Access Token 입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "존재하지 않는 Refresh Token 입니다."),
    NOT_FOUND_COOKIE(HttpStatus.NOT_FOUND, "쿠키 값이 존재하지 않습니다. 다시 로그인 해주세요."),
    INCORRECT_REFRESH_TOKEN(HttpStatus.CONFLICT, "Refresh Token 이 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다. 다시 로그인 해주세요."),

    // 상품 등록 및 조회 에러코드
    NOT_EQUAL_PRODUCT_USER(HttpStatus.BAD_REQUEST, "상품 수정/삭제 권한이 없습니다."),
    CHECK_USER_ID(HttpStatus.NOT_FOUND, "작성자 정보가 유효하지않습니다."),
    CHECK_PRODUCT_ID(HttpStatus.NOT_FOUND, "상품 정보가 유효하지않습니다."),
    NOT_ACCEPT(HttpStatus.NOT_ACCEPTABLE, "해당 페이지에 오류가 발생했습니다."),
    NOT_SAVE_FILE(HttpStatus.BAD_REQUEST, "파일 저장에 실패했습니다."),
    NOT_SAVE_TERMINATE(HttpStatus.BAD_REQUEST, "판매 종료일은 현날짜 이후로 설정 가능합니다."),
      
    // 상품 진열 에러코드
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    NOT_FOUND_STATUS(HttpStatus.NOT_FOUND, "해당 상태값은 존재하지 않습니다."),

    // 장바구니 에러코드
    NOT_AUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    // 주문 에러코드
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. 유저 정보를 확인해주세요."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND,"유저의 장바구니를 찾을 수 없습니다."),
    CART_ITEM_NOT_SELECTED(HttpStatus.NOT_FOUND,"장바구니에서 선택된 상품이 없습니다. 상품을 선택해주세요.")


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
