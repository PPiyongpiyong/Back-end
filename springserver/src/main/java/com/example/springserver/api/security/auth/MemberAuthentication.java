package com.example.springserver.api.security.auth;

import com.example.springserver.api.security.domain.MemberEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// 유효성을 검증 받고 난 뒤의 인증 객체
public class MemberAuthentication extends UsernamePasswordAuthenticationToken {

    // UsernamePasswordAuthenticationToken: 기본 인증 객체로 인증 요청과 인증 상태를 관리
    public MemberAuthentication(
            Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(MemberEntity member) {

        // authorizes 생성(List 타입)
        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new MemberAuthentication(
                member.getId(),
                member.getPassword(),
                authorities // 권한들 자체가 리스트 형식이기에 그냥 넣으면 됨
        );
    }
}
