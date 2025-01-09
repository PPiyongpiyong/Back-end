package com.example.springserver.api.EmergencyMap.Dto;

import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import lombok.Getter;

@Getter
public class HospitalInfo {
    private final String id;
    private final String placeName;
    private final String addressName;
    private final String roadAddressName;
    private final String categoryName;
    private final String phone;
    private final String x;
    private final String y;


    public HospitalInfo(String id, String placeName, String addressName, String roadAddressName,
                        String categoryName, String phone, String x, String y) {
        this.id = id;
        this.placeName = placeName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.categoryName = categoryName;
        this.phone = phone;
        this.x = x;
        this.y = y;
    }

    // KakaoCategorySearchResponse의 Document 객체를 HospitalInfo로 변환하는 메서드
    public static HospitalInfo of(Document document) {
        return new HospitalInfo(
                document.getId(),                   // 병원 ID
                document.getPlaceName(),            // 병원 이름
                document.getAddressName(),          // 주소
                document.getRoadAddressName(),      // 도로명 주소
                document.getCategoryName(),         // 카테고리
                document.getPhone(),                // 전화번호
                document.getX(),                    // X 좌표
                document.getY()                     // Y 좌표
        );
    }


}