package com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoCategorySearchResponse {
    private Meta meta;
    private List<Document> documents;
}