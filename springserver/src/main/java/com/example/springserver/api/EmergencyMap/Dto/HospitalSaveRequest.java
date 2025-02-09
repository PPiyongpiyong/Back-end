package com.example.springserver.api.EmergencyMap.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalSaveRequest {

    private String placeId;

    private String placeName;

    private String addressName;

    private String roadAddressName;

    private String categoryName;

    private String phone;

    private String x;

    private String y;

    public void validateFields() {
        if (placeId == null || placeId.isEmpty()) {
            throw new IllegalArgumentException("placeId 값이 필요합니다.");
        }

        if (placeName == null || placeName.isEmpty()) {
            throw new IllegalArgumentException("placeName 값이 필요합니다.");
        }

        if (addressName == null || addressName.isEmpty()) {
            throw new IllegalArgumentException("addressName 값이 필요합니다.");
        }

        if (roadAddressName == null || roadAddressName.isEmpty()) {
            throw new IllegalArgumentException("roadAddressName 값이 필요합니다.");
        }

        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("categoryName 값이 필요합니다.");
        }

        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("phone 값이 필요합니다.");
        }

        if (x == null || x.isEmpty()) {
            throw new IllegalArgumentException("x 값이 필요합니다.");
        }

        if (y == null || y.isEmpty()) {
            throw new IllegalArgumentException("y 값이 필요합니다.");
        }
    }
}