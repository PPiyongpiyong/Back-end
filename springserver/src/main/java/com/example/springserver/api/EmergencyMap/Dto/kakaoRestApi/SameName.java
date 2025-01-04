package com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SameName {
    private String[] region;

    private String keyword;

    @JsonProperty("selected_region")
    private String selectedRegion;
}