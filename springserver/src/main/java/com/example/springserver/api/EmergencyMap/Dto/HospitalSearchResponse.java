package com.example.springserver.api.EmergencyMap.Dto;

import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
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

    /**
     * 필터링된 병원 데이터를 포함하는 응답 생성 메서드
     */
    public static HospitalSearchResponse ofFiltered(KakaoCategorySearchResponse kakaoCategorySearchResponse, List<Document> filteredDocuments) {
        // 필터링된 병원 정보를 변환
        List<HospitalInfo> hospitalInfos = filteredDocuments.stream()
                .map(HospitalInfo::of)
                .toList();

        // 메타데이터 생성
        HospitalSearchMeta searchMeta = new HospitalSearchMeta(kakaoCategorySearchResponse.getMeta().getIsEnd(), kakaoCategorySearchResponse.getMeta().getPageableCount());

        // 필터링된 결과가 없을 경우 빈 리스트를 반환하여 응답을 만들도록 수정
        return new HospitalSearchResponse(searchMeta, hospitalInfos);
    }


}