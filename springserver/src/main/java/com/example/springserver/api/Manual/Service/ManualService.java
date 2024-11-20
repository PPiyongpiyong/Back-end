package com.example.springserver.api.Manual.Service;


import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Repository.ManualRepository;
import com.example.springserver.api.Manual.exception.ManualNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManualService {
    private final ManualRepository manualRepository;


    public ManualRespondDto getManualByEmergencyName(String emergencyName) {
        Manual manual = manualRepository.findByEmergencyName(emergencyName)
                .orElseThrow(() -> new ManualNotFoundException("매뉴얼을 찾을 수 없습니다: " + emergencyName));
        return new ManualRespondDto(manual.getEmergencyName(), manual.getManualSummary());
    }
}
