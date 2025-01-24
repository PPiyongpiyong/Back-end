package com.example.springserver.api.Manual.Service;
import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Repository.ManualCategoryRepository;
import com.example.springserver.api.Manual.Repository.ManualRepository;
import com.example.springserver.global.exception.impl.ManualNotFoundException;

import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;


@Service
@AllArgsConstructor
public class ManualService {

    
    private final ManualCategoryRepository manualCategoryRepository;


    private final Trie trie;
    private final ManualRepository manualRepository;

    // 매뉴얼 이름으로 조회

    public ManualRespondDto getManualByEmergencyName(String emergencyName) {
        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new ManualNotFoundException("매뉴얼을 찾을 수 없습니다: " + emergencyName));
        return new ManualRespondDto(manual.getEmergencyName(), manual.getManualSummary());
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
                            manual.getManualSummary() // manualSummaries
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
}
