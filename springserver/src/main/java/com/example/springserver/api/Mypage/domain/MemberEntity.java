package com.example.springserver.api.Mypage.domain;

import com.example.springserver.api.Mypage.dto.MypageReqeustDto;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "MEMBER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "member_id")
    private Long memberId; // 유저 DB 아이디

    private String username; // 유저의 실제 이름

    @Column(nullable = false, unique = true)
    private String email; // 유저 이메일

    private String password;

    // Enum 타입을 컬렉션으로 저장
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    private String phoneNumber;
    private String gender;
    private String parentPhoneNumber;
    private String address;
    private String residentNo;

    // 자체 로그인, 카카오 로그인 구분
    private String provider;

    // 즐겨찾기 병원 정보
    private List<String> favorites;

    // 위임 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 비밀번호 설정
    public void setPassword(String pw) {
        this.password = pw;
    }


    // 정보 수정하기
    public void updateMember(String phoneNumber, String username, String address, String parentPhoneNumber) {
        this.phoneNumber = StringUtils.isBlank(phoneNumber) ? this.phoneNumber : phoneNumber;
        this.username = StringUtils.isBlank(username) ? this.username : username;
        this.address = StringUtils.isBlank(address) ? this.address : address;
        this.parentPhoneNumber = StringUtils.isBlank(parentPhoneNumber) ? this.parentPhoneNumber : parentPhoneNumber;
    }
}