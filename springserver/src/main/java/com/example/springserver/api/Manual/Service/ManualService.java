package com.example.springserver.api.Manual.Service;
import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Repository.ManualCategoryRepository;
import com.example.springserver.api.Manual.Repository.ManualRepository;
import com.example.springserver.api.Manual.exception.ManualNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ManualService {
    private final ManualRepository manualRepository;
    private final ManualCategoryRepository manualCategoryRepository;

    // 메뉴얼
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
}
