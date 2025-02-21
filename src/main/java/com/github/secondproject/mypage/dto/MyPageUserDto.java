package com.github.secondproject.mypage.dto;

import com.github.secondproject.auth.entity.UserEntity;
import com.github.secondproject.auth.entity.UserImageEntity;
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
    private String address;
    private String phone;
    private String gender;
    private UserImageEntity userImage;

    public static MyPageUserDto fromMyPageUser(UserEntity userEntity, UserImageEntity userImageEntity) {
        return MyPageUserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .gender(userEntity.getGender())
                .userImage(userImageEntity)
                .build();
    }
}
