package com.example.springserver.global.Kakao.auth.Domain;
import jakarta.persistence.Entity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String platformId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String refreshToken;


    @Builder
    public User(String platformId, String nickname) {
        this.platformId = platformId;
        this.nickname = nickname;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
