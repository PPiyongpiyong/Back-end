package com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private String id;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_group_code")
    private String categoryGroupCode;

    @JsonProperty("category_group_name")
    private String categoryGroupName;

    private String phone;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    private String x;
    private String y;
    @JsonProperty("place_url")
    private String placeUrl;

    private String distance;

    private Boolean isFavorite = false;

    public void favorite() {
        this.isFavorite = true;
    }

}