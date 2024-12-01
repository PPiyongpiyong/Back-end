package com.example.springserver.api.security.auth;

import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.domain.constants.JwtValidationType;
import com.example.springserver.api.security.domain.constants.Role;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final ObjectMapper objectMapper; // java 객체의 다양 변환 가능
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_ROLES = "roles";


    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.access.expiration}") // 임의로 값 설정
    private long accessTokenExpiration;

    @Value("${spring.jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    // JWT 키 발급(토큰 생성에 사용)
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // token 생성
    public String generateToken(
            MemberEntity member, long expiration, boolean isRefresh) {

        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration * 1000); // 현재로부터 계산

            // roles를 문자열 리스트로 변형 필요(원래는 enum list)
            List<Role> roles = member.getRoles();
            List<String> rolesName = roles.stream()
                                            .map(Role::name) // enum의 name들(String) 가져오기
                                            .collect(Collectors.toList());

            // JWT(token) 을 builder를 통해 반환
            return Jwts.builder()
                    .setHeaderParam("type", isRefresh ? "refresh" : "access")
                    .setSubject(String.valueOf(member.getId()))
                    .claim("roles", rolesName)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("token 생성에 실패하였습니다.");
        }
    }

    // AccessToken 생성
    public String generateAccessToken(MemberEntity member) {

        return generateToken(member, accessTokenExpiration, false);
    }

    // RefreshToken 생성
    public String generateRefreshToken(MemberEntity member) {

        return generateToken(member, refreshTokenExpiration, true);
    }

    // RefreshToken redis 서버에 저장하기
    public void saveRefreshToken(String memberId, String refreshToken) {

        // redis 서버에 저장할 키
        String key = "refreshToken: " + memberId;
        // 저장하는 기한
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenExpiration, TimeUnit.SECONDS);
    }

    // RefreshToken 조회하기
    public String getRefreshToken(String memberId) {
        String key = "refreshToken: " + memberId;

        return (String) redisTemplate.opsForValue().get(key); // 멤버 고유 아이디로 확인
    }

    // Refresh Token 삭제하기
    public void deleteRefreshToken(String memberId) {
        String key = "refreshToken: " + memberId;
        redisTemplate.delete(key);
    }

    // 발급받은 Token으로부터 member의 아이디를 얻기
    public String getMemberIdFromToken(String token) throws CustomException {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String memberId = claims.getSubject();

        if(!StringUtils.hasText(memberId)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return memberId;
    }

    // Claim JWT 정보 얻어오기(최종적으로 JWT의 payload(Body)를 가져오는 것과 동일
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT를 검증하기 위한 메서드 ( 타입별로 반환 저장 )
    public JwtValidationType validateToken(String token) {
        try {
            final Claims claims = getClaims(token);
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) {
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) {
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            return JwtValidationType.EMPTY_JWT;
        }
    }
}
