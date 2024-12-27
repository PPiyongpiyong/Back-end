package com.example.springserver.api.security.auth.jwt;

import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


//userId(PK)를 Subject로 지정한 access token과 refresh token을 발급하고 지정된 Subject를 조회
@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtGenerator jwtGenerator;

    public Token issueToken(Long userId) {
        return Token.of(jwtGenerator.generateToken(userId, true),
                jwtGenerator.generateToken(userId, false));
    }

    public Long getSubject(String token) {
        JwtParser jwtParser = jwtGenerator.getJwtParser();
        return Long.valueOf(jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}