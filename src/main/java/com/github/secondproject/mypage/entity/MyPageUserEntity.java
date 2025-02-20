package com.github.secondproject.mypage.entity;

import com.github.secondproject.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@PrimaryKeyJoinColumn(name = "user_id")
public class MyPageUserEntity extends UserEntity {
    private String aboutMe;
    private String profileImageUrl;
}
