package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final KakaoHospitalApiClient kakaoHospitalApiClient;
    private static final Integer radius = 5000;
    private static final String distance = "distance";

    @Override

    public HospitalSearchResponse searchHospitals(Integer page, Integer size, String x, String y, String categoryName, String authToken) {
        if (size <= 0 || size > 15) {
            throw new IllegalArgumentException("size는 1~15 사이의 값이어야 합니다.");
        }

        // Kakao API 호출
        KakaoCategorySearchResponse categorySearchResponse = kakaoHospitalApiClient.searchHospitals(
                CategoryGroupCode.HP8.name(),
                x,
                y,
                categoryName,
                radius,
                page,
                size,
                distance
        );



        List<Document> filteredDocuments = categorySearchResponse.getDocuments().stream()
                .filter(document -> document.getCategoryName().contains(categoryName))
                .toList();



        return HospitalSearchResponse.ofFiltered(categorySearchResponse, filteredDocuments);
        // 필터링된 데이터를 기반으로 응답 생성

    }
}
