package com.example.springserver.api.EmergencyMap.Domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    private String placeName;

    private String addressName;

    private String roadAddressName;

    private String categoryName;

    private String phone;

    private String x;

    private String y;



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

    public static Hospital createHospital(String placeId, String placeName, String addressName, String roadAddressName, String categoryName, String phone, String x, String y) {
        return new Hospital(placeId, placeName, addressName, roadAddressName, categoryName, phone, x, y);
    }
}