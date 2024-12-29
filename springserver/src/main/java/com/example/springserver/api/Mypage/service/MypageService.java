package com.example.springserver.api.Mypage.service;

import com.example.springserver.api.Mypage.dto.MypageReqeustDto;
import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.dto.MemberMapper;
import com.example.springserver.api.security.dto.MemberResponseDto;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 회원 정보를 추가하기(수정)
    @Transactional
    public MemberResponseDto addMemberInfo(String token, MypageReqeustDto requestDto) throws CustomException {
        Long memberId = tokenProvider.getMemberIdFromToken(token);
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        member.updateMember(
                requestDto.phoneNumber(),
                requestDto.username(),
                requestDto.address(),
                requestDto.parentPhoneNumber()
        );
        memberRepository.save(member);
        return MemberMapper.toDto(member);
    }

    // 나의 정보 조회하기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo(String authToken) throws CustomException {
        Long memberId = tokenProvider.getMemberIdFromToken(authToken);
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));
        return MemberMapper.toDto(member);
    }
}
