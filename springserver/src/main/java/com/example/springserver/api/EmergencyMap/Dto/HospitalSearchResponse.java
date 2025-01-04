package com.example.springserver.api.EmergencyMap.Dto;

import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class HospitalSearchResponse {
    private HospitalSearchMeta meta;
    private List<HospitalInfo> hospitals;

    // 생성자 (private: 직접 호출하지 못하도록 함)
    private HospitalSearchResponse(HospitalSearchMeta meta, List<HospitalInfo> hospitals) {
        this.meta = meta;
        this.hospitals = hospitals;
    }


    public void FavoritedFilterResponse(List<HospitalInfo> hospitals) {
        this.hospitals = hospitals;
    }

    /**
     * 모든 병원 데이터를 포함하는 응답 생성 메서드
     */
    public static HospitalSearchResponse of(KakaoCategorySearchResponse kakaoCategorySearchResponse) {
        // 모든 병원 정보를 변환
        List<HospitalInfo> hospitalInfos = kakaoCategorySearchResponse.getDocuments().stream()
                .map(HospitalInfo::of)
                .toList();

        // 메타데이터 생성
        HospitalSearchMeta searchMeta = new HospitalSearchMeta(
                kakaoCategorySearchResponse.getMeta().getIsEnd(),
                kakaoCategorySearchResponse.getMeta().getPageableCount()
        );

        return new HospitalSearchResponse(searchMeta, hospitalInfos);
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
        HospitalSearchMeta searchMeta = new HospitalSearchMeta(
                kakaoCategorySearchResponse.getMeta().getIsEnd(),
                kakaoCategorySearchResponse.getMeta().getPageableCount()
        );

        return new HospitalSearchResponse(searchMeta, hospitalInfos);
    }
}
