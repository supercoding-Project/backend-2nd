package com.github.secondproject.mypage.dto;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.mypage.entity.MyPageUserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageUserDto {
    private Long userId;
    private String email;
    private String username;
    private String aboutMe; //자기소개
    private String address;
    private String phone;
    private String profileImageUrl;

    public static MyPageUserDto fromEntities(UserEntity userEntity, MyPageUserEntity myPageUserEntity) {
        return MyPageUserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .aboutMe(myPageUserEntity.getAboutMe())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .profileImageUrl(myPageUserEntity.getProfileImageUrl())
                .build();
    }
}
