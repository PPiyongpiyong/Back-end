package com.example.springserver.api.security.service;

import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.dto.*;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.springserver.api.security.domain.constants.JwtValidationType.VALID_JWT;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    // 회원가입 서비스
    @Transactional
    public MemberResponseDto register(MemberRequestDto requestDto) throws RuntimeException {

        // 이미 존재하는 아이디인지를 확인
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String pw = this.passwordEncoder.encode(requestDto.password());
        // 입력한 내용들에 대하여 입력
        MemberEntity member = MemberMapper.toEntity(requestDto);
        member.setPassword(pw);
        memberRepository.save(member); // 인코딩한 값으로 저장 필요

        return MemberMapper.toDto(member);
    }

    // 로그인 후 토큰 발급
    @Transactional
    public TokenDto GeneralLogin(LoginRequestDto requestDto) throws CustomException {

        // 1. 사용자 존재 여부 확인
        MemberEntity member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_ALREADY_EXISTS));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
        // 로그인 성공

        // 3. 토큰 발급
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);
        tokenProvider.saveRefreshToken(member.getEmail(), refreshToken); // redis-server에 저장

        // 4. TokenDto 반환
        return new TokenDto(accessToken, refreshToken);
    }

    // Refresh token 검증 후에 Access token 재발급
    @Transactional
    public String regenerateAccessToken(RefreshRequestDto request) {

        String refreshToken = request.getRefreshToken();

        // 검증 결과
        if (tokenProvider.validateToken(refreshToken) != VALID_JWT) {
            log.info("invlaid token");
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // 멤버 아이디 가져오기
        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);

        // Redis에서 Refresh Token 확인하기
        String storedRefreshToken = tokenProvider.getRefreshToken(refreshToken);
        if (!refreshToken.equals(storedRefreshToken)) { // redis 서버와 현재 저장이 다를 때
            throw new CustomException(ErrorCode.REFRESH_TOKEN_UNMATCHED);
        }

        // Access token을 Refresh 확인 후에 재발급
        String newAccessToken = tokenProvider.generateAccessToken(
                memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT)));
        return newAccessToken;
    }

}
