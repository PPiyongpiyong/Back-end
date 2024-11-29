package com.example.springserver.api.security.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {

    // UsernamePasswordAuthenticationToken: 기본 인증 객체로 인증 요청과 인증 상태를 관리
    public MemberAuthentication(
            Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(String memberId) {
        return new MemberAuthentication(memberId, null, null);
    }
}
