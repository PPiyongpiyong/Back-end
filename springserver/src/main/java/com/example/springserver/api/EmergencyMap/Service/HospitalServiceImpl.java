package com.example.springserver.api.EmergencyMap.Service;


import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchRequest;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * hospital 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final KakaoHospitalApiClient kakaoHospitalApiClient;
    private static final Integer radius = 5000;
    private static final String distance = "distance";


    @Override
    public HospitalSearchResponse searchHospitals(Integer page, Integer size, String x, String y) {

        if (size <= 0 || size > 15) {
            throw new IllegalArgumentException("size는 1~15 사이의 값이어야 합니다.");
        }

        KakaoCategorySearchResponse categorySearchResponse = kakaoHospitalApiClient.searchHospitals(
                CategoryGroupCode.HP8.name(),
                x,
                y,
                radius,
                page,
                size,
                distance
        );

        return HospitalSearchResponse.of(categorySearchResponse);
    }
}