package com.github.secondproject.mypage.service;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.repository.UserRepository;
import com.github.secondproject.mypage.dto.MyPageUserDto;
import com.github.secondproject.mypage.entity.MyPageUserEntity;
import com.github.secondproject.mypage.repository.MyPageUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final MyPageUserRepository myPageUserRepository;


    //유저 정보 조회
    @Transactional
    public List<MyPageUserDto> getMyPageUserDto(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        MyPageUserEntity myPageUserEntity = myPageUserRepository.findByUserEntity_UserId(userId)
                .orElse(new MyPageUserEntity());

        return List.of(MyPageUserDto.fromEntities(userEntity, myPageUserEntity));

    }

    //유저 정보 수정
    @Transactional
    public void updateMyPage(Long userId, MyPageUserDto myPageUserDto) {
        MyPageUserEntity myPageUserEntity = myPageUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "마이페이지 정보를 찾을 수 없습니다."));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"사용자 정보를 찾을 수 없습니다. "));

        myPageUserEntity.setAboutMe(myPageUserDto.getAboutMe());
        myPageUserRepository.save(myPageUserEntity);

        userEntity.setEmail(myPageUserDto.getEmail());
        userEntity.setAddress(myPageUserDto.getAddress());
        userEntity.setPhone(myPageUserDto.getPhone());
        userRepository.save(userEntity);

    }

    //TODO:MyPageCartList

    //TODO: MyPageOrderHistory

}
