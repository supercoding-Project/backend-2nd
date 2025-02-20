package com.github.secondproject.mypage.dto;

import com.github.secondproject.auth.entity.UserEntity;
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

    public static MyPageUserDto fromEntities(UserEntity userEntity) {
        return MyPageUserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .build();
    }
}
