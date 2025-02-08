package com.example.springserver.api.EmergencyMap.Domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hospital")
public class Hospital {

    // 추가된 메서드
    @Getter
    @Id
    @Column(unique = true, nullable = false)
    private String placeId;

    private String placeName;
    private String addressName;
    private String roadAddressName;
    private String categoryName;
    private String phone;
    private String x;
    private String y;

    // 기본 생성자 사용
    @Builder
    private Hospital(String placeId, String placeName, String addressName, String roadAddressName, String categoryName, String phone, String x, String y) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.categoryName = categoryName;
        this.phone = phone;
        this.x = x;
        this.y = y;
    }

    // Hospital 객체 생성 메소드
    public static Hospital createHospital(String placeId, String placeName, String addressName, String roadAddressName, String categoryName, String phone, String x, String y) {
        return Hospital.builder()
                .placeId(placeId)
                .placeName(placeName)
                .addressName(addressName)
                .roadAddressName(roadAddressName)
                .categoryName(categoryName)
                .phone(phone)
                .x(x)
                .y(y)
                .build();
    }

}