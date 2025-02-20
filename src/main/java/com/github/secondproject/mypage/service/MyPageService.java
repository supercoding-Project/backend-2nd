package com.github.secondproject.mypage.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.mypage.dto.MyPageCartListDto;
import com.github.secondproject.mypage.dto.MyPageOrderHistoryDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.entity.MyPageUserEntity;
import com.github.secondproject.mypage.repository.MyPageUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final MyPageUserRepository myPageUserRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    //유저 정보 조회
    @Transactional
    public MyPageUserDto getMyPageUserDto(Long userId) {
        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        // MyPageUserEntity 의 aboutMe, profileImage 가져오기
        MyPageUserEntity myPageUserEntity = myPageUserRepository.findByUserId(userId)
                .orElse(new MyPageUserEntity());

        if (myPageUserEntity.getAboutMe() == null) {
            myPageUserEntity.setAboutMe("");
        }
        if (myPageUserEntity.getProfileImageUrl() == null || myPageUserEntity.getProfileImageUrl().isEmpty()) {
            myPageUserEntity.setProfileImageUrl("Images/DefaultImage.png");
        }

        return MyPageUserDto.fromEntities(userEntity, myPageUserEntity);

    }

    //유저 정보 수정
    @Transactional
    public void updateMyPage(Long userId, MyPageUserDto myPageUserDto) {
        //마이 페이지 정보 가져오기
        MyPageUserEntity myPageUserEntity = myPageUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "마이페이지 정보를 찾을 수 없습니다."));

        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"사용자 정보를 찾을 수 없습니다. "));

        //AboutMe, ProfileImage 수정
        myPageUserEntity.setAboutMe(myPageUserDto.getAboutMe());
        myPageUserEntity.setProfileImageUrl(myPageUserDto.getProfileImageUrl());
        myPageUserRepository.save(myPageUserEntity);

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
            List<CartEntity> cartEntities = cartRepository.findByuserId(userId);

            if(cartEntities == null || cartEntities.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"장바구니에 상품이 존재하지 않습니다.");
            }
            return List.of(new MyPageCartListDto(cartEntities));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"장바구니 조회 중 오류가 발생했습니다.",e);
        }
    }

    //구매 내역 조회
    @Transactional
    public List<MyPageOrderHistoryDto> getMyPageOrderHistory(Long userId) {
        try {
            List<OrderEntity> orderEntities = orderRepository.findByuserId(userId);

            if (orderEntities == null || orderEntities.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "구매 내역이 존재하지 않습니다.");
            }

            return List.of(new MyPageOrderHistoryDto(orderEntities));

        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"구매 내역 조회 중 오류가 발생했습니다.", exception);
        }
    }
}
