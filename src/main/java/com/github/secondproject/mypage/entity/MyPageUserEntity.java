package com.github.secondproject.mypage.entity;

import com.github.secondproject.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mypage_user")
public class MyPageUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mypage_user_id")
    private Integer myPageUserId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mypage_user_id")
    private UserEntity userEntity;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "about_me", columnDefinition = "TEXT")
    private String aboutMe;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "profile_image", length = 255)
    private String profileImage;
}
