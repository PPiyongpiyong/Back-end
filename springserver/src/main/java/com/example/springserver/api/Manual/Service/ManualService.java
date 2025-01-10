package com.example.springserver.api.Manual.Service;
import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;

import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRequest.ManualKeywordRequest;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond.ManualKeywordRespond;
import com.example.springserver.api.Manual.Repository.ManualCategoryRepository;
import com.example.springserver.api.Manual.Repository.ManualRepository;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;

import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;

@Slf4j
@Service
@AllArgsConstructor
public class ManualService {

    
    private final ManualCategoryRepository manualCategoryRepository;
    private final Trie trie;
    private final ManualRepository manualRepository;

    // 인증
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 매뉴얼 이름으로 조회

    public ManualRespondDto getManualByEmergencyName(String emergencyName, String token) {
        // token 인증 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new CustomException(ErrorCode.MANUAL_NOT_FOUND));


        return new ManualRespondDto(manual.getEmergencyName(), manual.getManualSummary(), manual.getImgurl());
    }


    public List<ManualCategoryRespondDto> getManualByCategory(String category, String token) {

        // token 인증 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        List<Manual> manuals = manualCategoryRepository.findByCategory(category);

        // 카테고리 별로 매뉴얼들을 반환
        return manuals.stream()
                .map(manual -> {
                    // ManualCategoryRespondDto 객체 생성
                    return new ManualCategoryRespondDto(
                            manual.getCategory(), // category
                            manual.getEmergencyName(),
                            manual.getManualSummary(),
                            manual.getImgurl()// manualSummaries
                            // emergencyName
                    );
                })
                .collect(Collectors.toList());
    }

    // Trie에 키워드 저장 메서드
    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null); // key에는 입력 keyword, value는 null
    }

    // DB에 있는 매뉴얼 emergencyName 가져오기
    public void loadEmergencyNameIntoTrie(String token) {

        // token 인증 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        List<String> emergencyNames = manualRepository.findAllEmergencyNames();
        for (String name: emergencyNames) {
            addAutocompleteKeyword(name);
        }
    }

    // 저장한 Trie에서 이름 조회
    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream().collect(Collectors.toList()); // 받아온 것을 list로 변환
    }

    // Trie에 저장된 키워드 삭제
    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);

    }

    //세부 매뉴얼 조회
    public ManualDetailRespondDto getManualDetail(String emergencyName, String token) {

        // token 인증 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new CustomException(ErrorCode.MANUAL_NOT_FOUND));

        return new ManualDetailRespondDto(manual.getEmergencyName(), manual.getManualDetail());
    }
    //키워드 조회

    public List<ManualKeywordRespond> getManualByEmergencyKeyword(String keyword, String token) {

        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        List<Manual> manuals = manualRepository.findByDetailContaining(keyword);


        return manuals.stream()
                .map(manual -> new ManualKeywordRespond(
                        manual.getEmergencyName(),
                        manual.getManualSummary()
                ))
                .collect(Collectors.toList());
    }



}


