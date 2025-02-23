package com.github.secondproject.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id")
    private Long tokenId;

    @Column(name = "email")
    String email;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expiration")
    private String expiration;
}
