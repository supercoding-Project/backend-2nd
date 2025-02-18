package com.github.secondproject.mypage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageUserDto {
    private Integer userId; // 유저 ID
    private String userName; //유저 이름
    private String email; //이메일
    private String aboutMe; //자기소개
    private String address; //주소
    private String phoneNumber; //전화번호
    private String profileImageUrl; //프로필사진
}
