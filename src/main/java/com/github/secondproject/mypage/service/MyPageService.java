package com.github.secondproject.mypage.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.entity.UserImageEntity;
import com.github.secondproject.auth.repository.UserImageRepository;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.auth.service.UserImageService;
import com.github.secondproject.cart.entity.CartEntity;
import com.github.secondproject.cart.repository.CartRepository;
import com.github.secondproject.global.exception.AppException;
import com.github.secondproject.global.exception.ErrorCode;
import com.github.secondproject.mypage.dto.MyPageCartListDto;
import com.github.secondproject.mypage.dto.MyPageOrderHistoryDto;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.order.entity.OrderEntity;
import com.github.secondproject.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final UserImageService userImageService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    //유저 정보 조회
    @Transactional
    public MyPageUserDto getMyPageUserDto(Long userId) {
        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USERINFO,ErrorCode.NOT_FOUND_USERINFO.getMessage()));

        //삭제된 사용자인지 확인하기
        if(userEntity.getDeletedAt() != null){
            throw new AppException(ErrorCode.DELETE_USERINFO,ErrorCode.DELETE_USERINFO.getMessage());
        }

        // 이미지 가져오기
       UserImageEntity userImageEntity = Optional.ofNullable(userImageRepository.findByUserEntity(userEntity))
               .orElseGet(() -> {
                  UserImageEntity defaultImage = new UserImageEntity();
                  defaultImage.setUrl("src/main/resources/static/uploads/default-profile Image.png");
                  return defaultImage;
               });

        return MyPageUserDto.fromMyPageUser(userEntity,userImageEntity);
    }

    //유저 정보 수정
    @Transactional
    public MyPageUserDto updateMyPage(Long userId, MyPageUserDto myPageUserDto) {

        //사용자 정보 가져오기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USERINFO,ErrorCode.NOT_FOUND_USERINFO.getMessage()));

        if(userEntity.getDeletedAt() != null){
            throw new AppException(ErrorCode.DELETE_USERINFO,ErrorCode.DELETE_USERINFO.getMessage());
        }

        //email, username, address, phone, gender 정보 수정
        userEntity.setEmail(myPageUserDto.getEmail());
        userEntity.setUsername(myPageUserDto.getUsername());
        userEntity.setAddress(myPageUserDto.getAddress());
        userEntity.setPhone(myPageUserDto.getPhone());
        userEntity.setGender(myPageUserDto.getGender());
        userRepository.save(userEntity);

        //이미지 수정
        UserImageEntity userImageEntity = userImageRepository.findByUserEntity(userEntity);
        if(userImageEntity != null){
            //기존 이미지 삭제
            String imageUrl = userImageEntity.getUrl().replace("/uploads/profiles/" ,"src/main/resources/static/uploads/profiles/");
            Path deletePath = Paths.get(imageUrl);
            try{
                Files.deleteIfExists(deletePath); //이미지 파일 삭제
            }catch (IOException e){
                throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"프로필 삭제를 실패했습니다.",e); //실패시 오류
            }
            userImageRepository.delete(userImageEntity); //리포지토리에서 이미지 삭제
        }

        MultipartFile updateUserImage = (MultipartFile) myPageUserDto.getUserImage();
        userImageService.uploadUserImage(userEntity,updateUserImage);

        UserImageEntity updateUserImageEntities = userImageRepository.findByUserEntity(userEntity);
        if (updateUserImageEntities == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업데이트에 실패했습니다.");
        }
        return MyPageUserDto.fromMyPageUser(userEntity,updateUserImageEntities);
    }

    //장바구니 리스트 조회
    @Transactional
    public Optional<MyPageCartListDto> getMyPageCartList(Long userId) {
        // 사용자 장바구니 조회
        Optional<CartEntity> cartEntity = cartRepository.findByUserId(userId);

        // 장바구니가 존재하지 않으면 예외 처리
        cartEntity.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_CART_LIST, ErrorCode.NOT_FOUND_CART_LIST.getMessage()));

        return Optional.of(new MyPageCartListDto(cartEntity));
    }

    //구매 내역 조회
    @Transactional
    public Optional<MyPageOrderHistoryDto> getMyPageOrderHistory(Long userId) {
        //주문 내역 조회
        Optional<OrderEntity> orderEntities = orderRepository.findById(userId);

        if (orderEntities.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_ORDER_HISTORY,ErrorCode.NOT_FOUND_ORDER_HISTORY.getMessage());
        }

        return Optional.of(new MyPageOrderHistoryDto(orderEntities));
    }
}
