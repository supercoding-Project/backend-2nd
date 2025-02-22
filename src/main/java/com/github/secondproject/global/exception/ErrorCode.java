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
    NOT_FOUND_STATUS(HttpStatus.NOT_FOUND, "해당 상태값은 존재하지 않습니다."),

    // 장바구니 에러코드
    NOT_AUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    // 주문 에러코드
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. 유저 정보를 확인해주세요."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND,"유저의 장바구니를 찾을 수 없습니다."),
    CART_ITEM_NOT_SELECTED(HttpStatus.NOT_FOUND,"장바구니에서 선택된 상품이 없습니다. 상품을 선택해주세요."),

    // 결제 에러코드
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"주문 내역을 찾을 수 없습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"해당 주문에 대한 접근 권한이 없습니다."),
    INSUFFICIENT_FUNDS(HttpStatus.PAYMENT_REQUIRED,"예산이 부족하여 결제를 진행할 수 없습니다."),
    STOCK_VALIDATION_FAILED(HttpStatus.NOT_FOUND,"상품 재고 확인 중 오류가 발생했습니다."),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT,"선택하신 상품의 재고가 부족합니다."),
    PAYMENT_PROCESSING_ERROR(HttpStatus.CONFLICT,"결제 처리 중 오류가 발생했습니다.")




    ;

    private final HttpStatus httpStatus;
    private final String message;
}
