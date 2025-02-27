package com.example.springserver.global.security.service;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.global.exception.ErrorResponse;
import com.example.springserver.global.security.domain.constants.JwtValidationType;
import com.example.springserver.global.security.dto.*;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.springserver.global.security.domain.constants.JwtValidationType.VALID_JWT;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final MailService mailService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.refresh.expiration}")
    private long refreshTokenExpiration;

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
    public TokenDto GeneralLogin(
            LoginRequestDto requestDto,
            HttpServletResponse response
    ) throws CustomException {

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

        // 4. Refresh Token을 HttpOnly & Secure 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        refreshTokenCookie.setMaxAge((int)refreshTokenExpiration);
        response.addCookie(refreshTokenCookie);

        // 5. TokenDto 반환
        return new TokenDto(accessToken);
    }


    // refresh에 사용할 메소드
    @Transactional
    public ResponseEntity<?> refresh(
            HttpServletRequest request, HttpServletResponse response) {
        // 기록된 cookie에서 가져오기
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 저장된 쿠키에서 refresh token 부분 가져오기
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();

                    JwtValidationType result = tokenProvider.validateToken(refreshToken);

                    // 경우에 따른 반환
                    if (result == JwtValidationType.EXPIRED_JWT_TOKEN) {
                        try {
                            String newAccessToken = regenerateAccessToken(refreshToken);

                            return ResponseEntity.ok(
                                    RefreshResponseDto.builder()
                                    .accessToken(newAccessToken).build());
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
            }
        } else {
            throw new CustomException(ErrorCode.COOKIE_IS_NULL);
        }
        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_REFRESH_TOKEN);
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
    public void logout(HttpServletResponse response, String token) {

        Long memberId = tokenProvider.getMemberIdFromToken(token);
        // 쿠키에서 삭제 + null 값 채우기
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 즉시 만료시키기
        response.addCookie(refreshTokenCookie);

        // redis 서버에서까지 완전 삭제
        tokenProvider.deleteRefreshToken(memberId);
    }

    // 계정 탈퇴
    @Transactional
    public void delete(String token) {

        Long memberId = tokenProvider.getMemberIdFromToken(token);

        // redis-server에 저장된 것 삭제
        tokenProvider.deleteRefreshToken(memberId);

        // MemberEntity에서도 삭제
        MemberEntity findMember = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));
        memberRepository.delete(findMember);
    }

    //
    @Transactional
    public void sendVerificationCode(VerificationRequestDto requestDto) {

        // 존재 여부 확인
        MemberEntity findMember = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        // 인증코드
        String code = mailService.createCode();

        // 인증을 redis-server에 저장하기
        saveVerificationCode(findMember.getMemberId(), code);

        // 이메일 전송
        try {
            mailService.mailSend(requestDto, code);
            // 보낸 시각 기록
            log.info(findMember.getEmail() + " : " + "sendCodeMail" + "(" + new Date() + ")");
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패하였습니다.");
        }
    }

    // redis 서버에 저장
    public void saveVerificationCode(long memberId, String code) {
        String key = "verificationCode:" + memberId;

        /* 인증코드 유호 시간 2분*/
        long codeValidityInMilliseconds = 120 * 1000;

        redisTemplate.opsForValue().set(key, code, codeValidityInMilliseconds, TimeUnit.SECONDS);
    }

    // 인증코드 검증하기
    public void verify(
            VerificationRequestDto request, String code
    ) {
        MemberEntity member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));
        long memberId = member.getMemberId();

        // redis-server에 저장된 인증 코드 가져오기
        String key = "verificationCode:" + memberId;
        try {
            redisTemplate.opsForValue().get(key).equals(code);
            log.info("인증 코드 검증 성공");
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.CODE_UNMATCHED);
        };
    }

    // 비밀번호 변경하기
    @Transactional
    public String change(PasswordRequestDto request) {

        MemberEntity member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));
        long memberId = member.getMemberId();

        // 저장되어있는 인증 코드 삭제하기
        deleteVerificationCode(memberId);

        /* 비밀번호 변경 */
        try {
            String password = request.password();
            String encodePassword = passwordEncoder.encode(password);
            member.setPassword(encodePassword);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        /*성공 시 쿠키 초기화*/
        ResponseCookie responseCookie = ResponseCookie.from("temporary-token", null)
                .maxAge(0)
                .path("/")
                .build();
        return "성공적으로 변경 되었습니다.";
    }

    // 인증코드 유효 시간보다 빠르게 완료 시 삭제
    public void deleteVerificationCode(long memberId) {
        String key = "verificationCode:" + memberId;
        redisTemplate.delete(key);
    }
}
