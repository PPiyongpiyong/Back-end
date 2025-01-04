package com.example.springserver.api.Manual.Mapper;

import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRequest.ManualRequestDto;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRequest.ManualCategoryRequestDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRequest.ManualDetailRequestDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;

public class ManualDetailMapper {

    // ManualDetailRequestDto를 Manual 엔티티로 변환
    public static Manual toManualEntity(ManualDetailRequestDto manualDetailRequestDto) {
        return Manual.builder()
                .emergencyName(manualDetailRequestDto.getEmergencyName())
                .build();
    }

    // Manual 엔티티를 ManualDetailRespondDto로 변환
    public static ManualDetailRespondDto toManualDto(Manual manual) {
        return ManualDetailRespondDto.builder()
                .emergencyName(manual.getEmergencyName())
                .manualDetail(manual.getManualDetail())
                .build();
    }
}








