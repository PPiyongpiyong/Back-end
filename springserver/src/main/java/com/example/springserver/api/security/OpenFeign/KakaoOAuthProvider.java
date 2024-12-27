package com.example.springserver.api.security.OpenFeign;


import com.example.springserver.global.Kakao.auth.Error.KaKaoUnauthorizedException;
import com.example.springserver.api.security.OpenFeign.dto.KakaoAccessTokenInfo;
import com.example.springserver.global.Kakao.auth.OpenFeign.dto.KakaoFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.springserver.api.security.OpenFeign.KakaoAccessToken.createKakaoAccessToken;

@RequiredArgsConstructor
@Component
public class KakaoOAuthProvider {
    private final KakaoFeignClient kakaoFeignClient;

    public String getKakaoPlatformId(String accessToken) {
        KakaoAccessToken kakaoAccessToken = createKakaoAccessToken(accessToken);
        String accessTokenWithTokenType = kakaoAccessToken.getAccessTokenWithTokenType();
        KakaoAccessTokenInfo kakaoAccessTokenInfo = getKakaoAccessTokenInfo(accessTokenWithTokenType);
        return String.valueOf(kakaoAccessTokenInfo.getId());
    }

    private KakaoAccessTokenInfo getKakaoAccessTokenInfo(String accessTokenWithTokenType) {
        try {
            return kakaoFeignClient.getKakaoAccessTokenInfo(accessTokenWithTokenType);
        } catch (FeignException e) {
            throw new KaKaoUnauthorizedException(ErrorStatus.INVALID_KAKAO_ACCESS_TOKEN);
        }
    }
}