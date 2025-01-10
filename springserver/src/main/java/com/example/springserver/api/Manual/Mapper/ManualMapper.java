package com.example.springserver.api.Manual.Mapper;


import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.Manual.ManualRequest.ManualRequestDto;
import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;

public class ManualMapper {

    public static Manual toManualEntity(ManualRequestDto manualRequestDto) {
        return Manual.builder()
                .emergencyName(manualRequestDto.getEmergencyName())
                .build();
    }

    public static ManualRespondDto toManualDto(Manual manual){
        return ManualRespondDto.builder()
                .emergencyName(manual.getEmergencyName())

                .emergencyResponseSummary(manual.getManualSummary())
                .emergencyImage(manual.getImgurl())
                .build();
    }
}
