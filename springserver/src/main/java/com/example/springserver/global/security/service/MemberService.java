package com.example.springserver.global.security.service;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.global.security.domain.constants.JwtValidationType;
import com.example.springserver.global.security.dto.*;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;

import static com.example.springserver.global.security.domain.constants.JwtValidationType.VALID_JWT;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
        // 로그인 성공

        // 3. 토큰 발급
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);
        tokenProvider.saveRefreshToken(member.getMemberId(), refreshToken); // redis-server에 저장

        // 4. TokenDto 반환
        return new TokenDto(accessToken, refreshToken);
    }


    // refresh에 사용할 메소드
    @Transactional
    public RefreshResponseDto refresh(RefreshRequestDto request) {
        JwtValidationType result = tokenProvider.validateToken(request.getAccessToken());

        // 경우에 따른 반환
        if (result == JwtValidationType.EXPIRED_JWT_TOKEN) {
            try {
                String newAccessToken = regenerateAccessToken(request.getRefreshToken());

                return RefreshResponseDto.builder()
                        .accessToken(newAccessToken).build();
            } catch (Exception e) {
                // 기타 예외 처리
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        } else if (result == JwtValidationType.VALID_JWT) {
            // Access Token이 여전히 유효한 경우
            throw new CustomException(ErrorCode.TOKEN_UNEXPIRED);
        } else {
            // Access Token이 유효하지 않은 경우 (변조된 토큰 등)
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    // Refresh token 검증 후에 Access token 재발급
    private String regenerateAccessToken(String refreshToken) {

        // 검증 결과
        if (tokenProvider.validateToken(refreshToken) != VALID_JWT) {
            log.info("invlaid refresh token");
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // 멤버 아이디 가져오기
        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);

        // Redis에서 Refresh Token 확인하기
        String storedRefreshToken = tokenProvider.getRefreshToken(memberId).substring(13); // 저장된 refreshToken 가져오기
        if (!refreshToken.equals(storedRefreshToken)) { // redis 서버와 현재 저장이 다를 때
            throw new CustomException(ErrorCode.REFRESH_TOKEN_UNMATCHED);
        }

        // Access token을 Refresh 확인 후에 재발급
        String newAccessToken = tokenProvider.generateAccessToken(
                memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT)));
        return newAccessToken;
    }

    // 로그아웃
    @Transactional
    public void logout(LogoutRequestDto requestDto) {
        MemberEntity member = memberRepository.findByEmail(requestDto.getEmail())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        // password 확인

        tokenProvider.deleteRefreshToken(member.getMemberId());
    }

    // 계정 탈퇴
    @Transactional
    public void delete(long memberId) {
        // redis-server에 저장된 것 삭제
        tokenProvider.deleteRefreshToken(memberId);
        // MemberEntity에서도 삭제
        MemberEntity findMember = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));
        memberRepository.delete(findMember);
    }
}
