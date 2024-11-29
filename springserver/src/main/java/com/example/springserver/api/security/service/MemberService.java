package com.example.springserver.api.security.service;

import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.dto.*;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.global.exception.impl.AlreadyExistUserException;
import com.example.springserver.global.exception.impl.PasswordUnmatchedException;
import com.example.springserver.global.exception.impl.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 회원가입 서비스
    public TokenDto register(MemberRequestDto requestDto) throws RuntimeException {

        // 이미 존재하는 유저인지를 확인
        if (memberRepository.existsById(requestDto.id())) {
            throw new AlreadyExistUserException();
        }

        // 입력한 내용들에 대하여 입력
        MemberEntity member = memberRepository.save(
                MemberEntity.builder()
                        .id(requestDto.id())
                        .build());

        // 각각의 토큰 생성(Token Provider 사용)
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);

        return new TokenDto(accessToken, refreshToken);
    }

    // 회원 정보를 추가하기
    @Transactional
    public MemberResponseDto addMemberInfo(String token, MemberRequestDto requestDto) throws RuntimeException {
        String memberId = tokenProvider.getMemberIdFromToken(token);
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException());

        MemberEntity savedMember = memberRepository.save(MemberEntity.builder()
                .phoneNumber(requestDto.phoneNumber())
                .gender(requestDto.gender())
                .parentPhoneNumber(requestDto.parentPhoneNumber())
                .address(requestDto.address())
                .residentNo(requestDto.residentNo())
                .build());

        return MemberMapper.toDto(savedMember);
    }

    // 나의 정보 조회하기
    public MemberResponseDto getMyInfo(String authToken) throws RuntimeException {
        String memberId = tokenProvider.getMemberIdFromToken(authToken);
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException());
        return MemberMapper.toDto(member);
    }

    // 로그인 후 토큰 발급
    public TokenDto GeneralLogin(LoginRequestDto requestDto) throws RuntimeException {

        // 1. 사용자 존재 여부 확인
        MemberEntity member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new UserNotFoundException());

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new PasswordUnmatchedException();
        }
        // 로그인 성공
        // 3. 토큰 발급
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);

        // 4. TokenDto 반환
        return new TokenDto(accessToken, refreshToken);
    }

}
