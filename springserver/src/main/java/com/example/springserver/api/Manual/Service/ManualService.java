package com.example.springserver.api.Manual.Service;
import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Domain.ManualFavorite;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;

import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRequest.ManualKeywordRequest;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond.ManualKeywordRespond;
import com.example.springserver.api.Manual.Repository.ManualCategoryRepository;
import com.example.springserver.api.Manual.Repository.ManualFavoriteRepository;
import com.example.springserver.api.Manual.Repository.ManualRepository;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;

import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Service
@AllArgsConstructor

public class ManualService {

    
    private final ManualCategoryRepository manualCategoryRepository;
    private final Trie trie;
    private final ManualRepository manualRepository;
    private final ManualFavoriteRepository manualFavoriteRepository;
    // 인증
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 매뉴얼 이름으로 조회

    public ManualRespondDto getManualByEmergencyName(String emergencyName) {

        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new CustomException(ErrorCode.MANUAL_NOT_FOUND));


        return new ManualRespondDto(manual.getEmergencyName(), manual.getManualSummary(), manual.getImgurl());
    }


    public List<ManualCategoryRespondDto> getManualByCategory(String category) {

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
    public void loadEmergencyNameIntoTrie() {

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
    public ManualDetailRespondDto getManualDetail(String emergencyName) {

        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new CustomException(ErrorCode.MANUAL_NOT_FOUND));

        return new ManualDetailRespondDto(manual.getEmergencyName(), manual.getManualDetail());
    }

    //키워드 조회
    public List<ManualKeywordRespond> getManualByEmergencyKeyword(String keyword) {

        List<Manual> manuals = manualRepository.findByDetailContaining(keyword);


        return manuals.stream()
                .map(manual -> new ManualKeywordRespond(
                        manual.getEmergencyName(),
                        manual.getManualSummary(),
                        manual.getImgurl()
                ))
                .collect(Collectors.toList());
    }
    // 즐겨찾기 추가

    public ManualFavorite addFavorite(String emergencyName) {
        // 이미 즐겨찾기에 추가된 경우 예외 처리
        if (manualFavoriteRepository.findByEmergencyName(emergencyName).isPresent()) {
            throw new IllegalArgumentException("이미 즐겨찾기에 추가된 항목입니다.");
        }

        // 기존 매뉴얼이 있는지 확인
        Manual existingManual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new IllegalArgumentException("해당 매뉴얼이 존재하지 않습니다."));

        // 새로운 즐겨찾기 엔티티 생성
        ManualFavorite manualFavorite = ManualFavorite.builder()
                .emergencyName(existingManual.getEmergencyName())
                .imgurl(existingManual.getImgurl())
                .manualSummary(existingManual.getManualSummary())
                .manualDetail(existingManual.getManualDetail())
                .build();

        // 즐겨찾기 저장
        return manualFavoriteRepository.save(manualFavorite);
    }

    //즐겨찾기 삭제
    public void deleteFavorite(String emergencyName) {
        // 해당 응급상황이 즐겨찾기에 존재하는지 확인
        ManualFavorite manualFavorite = manualFavoriteRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 존재하지 않습니다."));

        // 즐겨찾기 삭제
        manualFavoriteRepository.delete(manualFavorite);
    }

    //즐겨찾기 조회
    public List<ManualFavorite> getFavorites() {
        return manualFavoriteRepository.findAll();
    }

}


