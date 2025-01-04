package com.example.springserver.global.security.domain.constants;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    // 사용 ROLE 설정, Spring에서의 양식 지키기(수정 가능하긴 함)
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_MANAGER;

    // GrantedAuthority로 변환
    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
