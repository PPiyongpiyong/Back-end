package com.example.springserver.global.security.domain;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

// 일반 로그인과 소셜 로그인을 통한 사용자의 주요 정보 획득
@AllArgsConstructor
@ToString
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private MemberEntity member;
    private Map<String, Object> oauth2UserAttributes;

    public UserPrincipal(MemberEntity member) {
        this.member = member;
    }

    // OAuth2 로그인 사용(Attributes 매개변수 필요 - provider)
    public static UserPrincipal create(MemberEntity member, Map<String, Object> oauth2UserAttributes) {
        return new UserPrincipal(member, oauth2UserAttributes);
    }

    // 일반 로그인 사용
    public static UserPrincipal create(MemberEntity member) {
        return new UserPrincipal(member, new HashMap<>());
    }

    public MemberEntity getMember() {
        return this.member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        // 실제 username이 아닌 PK가 되는 것을 반환
        return String.valueOf(this.member.getMemberId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Nullable
    public <A> A getAttribute(String name) {
        return (A) oauth2UserAttributes.get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(oauth2UserAttributes);
    }

    @Override
    public String getName() {
        // 실제 사용자의 이름을 얻고 싶을 때
        return member.getUsername();
    }
}
