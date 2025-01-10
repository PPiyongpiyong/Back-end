package com.example.springserver.api.Manual.Mapper;

import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRequest.ManualRequestDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRequest.ManualCategoryRequestDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
//
public class ManualCategoryMapper {
    public static Manual toEntity(ManualCategoryRequestDto manualCategoryRequestDto){
        return Manual.builder()
                .emergencyName(manualCategoryRequestDto.getEmergencyName())
                .category(manualCategoryRequestDto.getCategory())
                .manualSummary(manualCategoryRequestDto.getManualSummary())
                .build();
    }

    public static ManualCategoryRespondDto toManualCategoryDto(Manual manual){
        return ManualCategoryRespondDto.builder()
                .category(manual.getEmergencyName())
                .category(manual.getImgurl())
                // 여기 모르겠다..

                .build();
    }
}
