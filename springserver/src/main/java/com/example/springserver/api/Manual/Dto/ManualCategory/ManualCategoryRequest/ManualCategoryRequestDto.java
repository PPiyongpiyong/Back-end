package com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualCategoryRequestDto {
    private String emergencyName;
    private String manualSummary;
    private String category;
    // 생성자

}
