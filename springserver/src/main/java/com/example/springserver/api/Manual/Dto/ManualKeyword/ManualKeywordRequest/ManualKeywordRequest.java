package com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualKeywordRequest {
    private String keyword;
}
