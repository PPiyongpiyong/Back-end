package com.example.springserver.api.security.domain;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface UserDetails extends Serializable {
    // Spring Security에서 사용자의 상세 정보를 제공해주는 인터페이스

    // 권한 정보
    Collection<? extends GrantedAuthority> getAuthorities();

    // 유저의 이름
    String getUsername();

    // 유저의 비밀번호
    String getPassword();
}
