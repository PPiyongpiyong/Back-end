package com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualCategoryRespondDto {
    private String category;  // 카테고리 정보
    private String emergencyName;
    private String emergencyResponseSummary;
    private String emergencyImage;

}
