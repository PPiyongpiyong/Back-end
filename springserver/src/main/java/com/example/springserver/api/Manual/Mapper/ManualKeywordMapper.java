package com.example.springserver.api.Manual.Mapper;

import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRequest.ManualDetailRequestDto;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRequest.ManualKeywordRequest;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond.ManualKeywordRespond;

public class ManualKeywordMapper {
    public static Manual toKeyword(ManualKeywordRequest manualKeywordRequest){
        return Manual.builder()
                .manualDetail(manualKeywordRequest.getKeyword())
                .build();
    }
    public static ManualKeywordRespond toKeywordDto(Manual manual){
        return ManualKeywordRespond.builder()
                .emergencyName(manual.getEmergencyName())
                .emergencyName(manual.getManualDetail())
                .emergencyResponseSummary(manual.getManualSummary())
                .build();
    }

}



