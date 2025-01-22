package com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualKeywordRespond {
    private String emergencyName;
    private String emergencyResponseSummary;
    private String emergencyImage;
}
