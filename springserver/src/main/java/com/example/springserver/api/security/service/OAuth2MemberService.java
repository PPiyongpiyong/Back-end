package com.example.springserver.api.security.service;

import com.example.springserver.api.security.domain.KakaoUtils;
import com.example.springserver.api.security.domain.OAuth2Attribute;
import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.domain.UserPrincipal;
import com.example.springserver.api.security.dto.AuthConverter;
import com.example.springserver.api.security.dto.KakaoDto;
import com.example.springserver.api.security.dto.MemberResponseDto;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final KakaoUtils kakaoUtils;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Value("${kakao.api.key}")
    private String restApiKey;

    private final String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/ouath/token";

    // OAuth2MemberService
    public String responseUrl() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + restApiKey + "&redirect_uri=" + redirect_uri + "&response_type=code";

        return kakaoLoginUrl;
    }

    // 로그인
    public MemberEntity kakaoLogin(String accessCode, HttpServletResponse httpServletResponse){
        KakaoDto.OAuthToken oAuthToken = kakaoUtils.requestToken(accessCode);
        KakaoDto.KakaoProfile kakaoProfile = kakaoUtils.requestProfile(oAuthToken);
        String email = kakaoProfile.getKakao_account().getEmail();

        MemberEntity member = memberRepository.findByEmail(email)
                .orElseGet(() -> createNewMember(kakaoProfile));

        String token = tokenProvider.generateAccessToken(member);
        httpServletResponse.setHeader("Authorization", token);

        return member;
    }

    // 없으면 생성하는 메소드(in kakao)
    private MemberEntity createNewMember(KakaoDto.KakaoProfile kakaoProfile) {
        MemberEntity member = AuthConverter.toMember(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getKakao_account().getProfile().getNickname(),
                null,
                bCryptPasswordEncoder
        );
        return memberRepository.save(member);
    }


    // 위임 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 소셜로그인을 통한 유저 정보 로드
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 어떤 소셜인지 정보 얻어오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.debug("registrationId: {}", registrationId);
        log.debug("userNameAttributeName: " + userNameAttributeName);

        // OAuth2UserService를 통해 가져온 데이터를 담을 클래스
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, oAuth2User.getAttributes()); // provider, attributes

        // 기존 회원이면 update, 신규 회원이면 create
        MemberEntity member = saveOrUpdate(oAuth2Attribute);

        return UserPrincipal.create(member, oAuth2Attribute.getAttributes());
    }

    // DB에 접근
    public MemberEntity saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        // email과 provider로 조회하기
        // 없으면 oAuth2Attribute의 정보를 기반으로 새로운 멤버 가입
        MemberEntity member = memberRepository.findByEmailAndProvider(oAuth2Attribute.getEmail(), oAuth2Attribute.getProvider())
                .orElse(oAuth2Attribute.toEntity()); // MemberEntity 객체로 변환

        return memberRepository.save(member);
    }


}
