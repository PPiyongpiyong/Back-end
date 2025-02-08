package com.example.springserver.api.Manual.Dto.ManualFavorite.ManualFavoriteRespond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualPostRespond {
    private String category;  // 카테고리 정보
    private String emergencyName;
    private String emergencyResponseSummary;
    private String emergencyImage;
}
