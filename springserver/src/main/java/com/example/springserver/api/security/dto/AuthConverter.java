package com.example.springserver.api.security.dto;

import com.example.springserver.api.security.domain.MemberEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

public class AuthConverter {

    public static MemberEntity toMember(String email, String username, String password, BCryptPasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .email(email)
                .roles(Collections.singletonList("ROLE_USER"))
                .password(passwordEncoder.encode(password))
                .username(username)
                .build();
    }
}
