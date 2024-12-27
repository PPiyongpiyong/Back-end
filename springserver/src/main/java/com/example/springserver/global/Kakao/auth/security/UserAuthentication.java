package com.example.springserver.global.Kakao.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//사용자 정보를 저장한 인증의 주체이다. JWT를 사용하기 때문에 userId(PK)만 principal로 저장하여 사용
public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    private UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static UserAuthentication createDefaultUserAuthentication(Long userId) {
        return new UserAuthentication(userId, null, null);
    }
}