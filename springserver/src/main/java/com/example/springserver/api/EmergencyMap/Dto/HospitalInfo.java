package com.example.springserver.api.EmergencyMap.Dto;


import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import lombok.Getter;

/**
 * 이름, 주소, 번호
 */
@Getter
public class HospitalInfo {
    private final String id;
    private final String placeName;
    private final String addressName;
    private final String roadAddressName;
    private final String phone;
    private final String x;
    private final String y;

    protected HospitalInfo(String id, String placeName, String addressName, String roadAddressName, String phone, String x, String y) {
        this.id = id;
        this.placeName = placeName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.x = x;
        this.y = y;
    }

    public static HospitalInfo of(Document document) {
        return new HospitalInfo(document.getId(), document.getPlaceName(), document.getAddressName(), document.getRoadAddressName(), document.getPhone(), document.getX(), document.getY());
    }
}