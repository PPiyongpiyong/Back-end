package com.example.springserver.global.security.domain;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

// 사용자의 정보를 담을 클래스
// attribute = {stringId, userNameAttribute, email, ...}
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes; // Map 형식의 속성
    private String email; // 소셜에서 받아오는 이메일
    private String name; // 회원의 이름
    private String provider; // 소셜의 이름

    // attribute의 provider를 통해 어떤 소셜인지를 정보 얻어오기
    public static OAuth2Attribute of(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case"kakao" :
                return ofKakao(attributes);
            default:
                throw new RuntimeException();
        }
    }

    // 카카오
    private static OAuth2Attribute ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .attributes(kakaoAccount)
                .provider("kakao")
                .build();
    }

    // Mapper method
    public MemberEntity toEntity() {
        return MemberEntity
                .builder()
                .email(this.getEmail())
                .password(null)
                .username(this.getName())
                .provider(this.provider)
                .build();
    }

}
