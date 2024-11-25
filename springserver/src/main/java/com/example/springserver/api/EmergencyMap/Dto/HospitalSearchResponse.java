package com.example.springserver.api.EmergencyMap.Dto;

import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Meta;
import lombok.Getter;

import java.util.List;

@Getter
public class HospitalSearchResponse {
    private final HospitalSearchMeta meta;
    private final List<HospitalInfo> hospitals;

    private HospitalSearchResponse(HospitalSearchMeta meta, List<HospitalInfo> hospitals) {
        this.meta = meta;
        this.hospitals = hospitals;
    }

    public static HospitalSearchResponse of(KakaoCategorySearchResponse kakaoCategorySearchResponse) {

        List<HospitalInfo> hospitalInfos = kakaoCategorySearchResponse.getDocuments().stream()
                .map(HospitalInfo::of)
                .toList();

        HospitalSearchMeta searchMeta = new HospitalSearchMeta(kakaoCategorySearchResponse.getMeta().getIsEnd(), kakaoCategorySearchResponse.getMeta().getPageableCount());

        return new HospitalSearchResponse(
                searchMeta,
                hospitalInfos
        );
    }


}