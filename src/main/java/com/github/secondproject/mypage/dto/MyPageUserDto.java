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
    private UserImageEntity userImage;
    private UserEntity user;
}
