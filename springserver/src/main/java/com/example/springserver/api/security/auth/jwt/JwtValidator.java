package com.example.springserver.api.security.auth.jwt;


import com.example.springserver.global.Kakao.auth.Error.KaKaoUnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtValidator {
    private final JwtGenerator jwtGenerator;

    public void validateAccessToken(String accessToken) {
        try {
            JwtParser jwtParser = jwtGenerator.getJwtParser();
            jwtParser.parseClaimsJws(accessToken);
        } catch (ExpiredJwtException e) {
            throw new KaKaoUnauthorizedException(ErrorStatus.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new KaKaoUnauthorizedException(ErrorStatus.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            JwtParser jwtParser = jwtGenerator.getJwtParser();
            jwtParser.parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new KaKaoUnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new KaKaoUnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    public void equalsRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new KaKaoUnauthorizedException(ErrorStatus.NOT_MATCH_REFRESH_TOKEN);
        }
    }
}