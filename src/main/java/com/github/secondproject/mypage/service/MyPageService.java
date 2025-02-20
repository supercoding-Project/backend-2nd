package com.github.secondproject.mypage.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.cart.entity.CartEntity;
import com.github.secondproject.cart.repository.CartRepository;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.mypage.dto.MyPageCartListDto;
import com.github.secondproject.mypage.dto.MyPageOrderHistoryDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.repository.MyPageUserRepository;
import com.github.secondproject.order.entity.OrderEntity;
import com.github.secondproject.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    //유저 정보 조회
    @Transactional
    public MyPageUserDto getMyPageUserDto(Long userId) {
        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USERINFO,ErrorCode.NOT_FOUND_USERINFO.getMessage()));

        if(userEntity.getDeletedAt() != null){
            throw new AppException(ErrorCode.DELETE_USERINFO,ErrorCode.DELETE_USERINFO.getMessage());
        }

        return MyPageUserDto.fromEntities(userEntity);

    }

    //유저 정보 수정
    @Transactional
    public void updateMyPage(Long userId, MyPageUserDto myPageUserDto) {

        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USERINFO,ErrorCode.NOT_FOUND_USERINFO.getMessage()));

        if(userEntity.getDeletedAt() != null){
            throw new AppException(ErrorCode.DELETE_USERINFO,ErrorCode.DELETE_USERINFO.getMessage());
        }

        //email, username, address, phone 정보 수정
        userEntity.setEmail(myPageUserDto.getEmail());
        userEntity.setUsername(myPageUserDto.getUsername());
        userEntity.setAddress(myPageUserDto.getAddress());
        userEntity.setPhone(myPageUserDto.getPhone());
        userRepository.save(userEntity);

    }

    //장바구니 리스트 조회
    @Transactional
    public List<MyPageCartListDto> getMyPageCartList(Long userId) {
        try {
            Optional<CartEntity> cartEntities = cartRepository.findByUserId(userId);

            if(cartEntities.isEmpty()){
                throw new AppException(ErrorCode.NOT_FOUND_CART_LIST,ErrorCode.NOT_FOUND_CART_LIST.getMessage());
            }

            return Optional.of(new MyPageCartListDto(cartEntities));
        }catch (Exception e){
            throw new AppException(ErrorCode.MY_PAGE_CART_ERROR,ErrorCode.MY_PAGE_CART_ERROR.getMessage());
        }
    }

    //구매 내역 조회
    @Transactional
    public List<MyPageOrderHistoryDto> getMyPageOrderHistory(Long userId) {
        try {
            Optional<OrderEntity> orderEntities = orderRepository.findById(userId);

            if (orderEntities.isEmpty()) {
                throw new AppException(ErrorCode.NOT_FOUND_ORDER_HISTORY,ErrorCode.NOT_FOUND_ORDER_HISTORY.getMessage());
            }

            return List.of(new MyPageOrderHistoryDto(orderEntities));

        }catch (Exception exception){
            throw new AppException(ErrorCode.MY_PAGE_ORDER_ERROR,ErrorCode.MY_PAGE_ORDER_ERROR.getMessage());
        }
    }
}
