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
    public HospitalSearchResponse searchHospitals(HospitalSearchRequest hospitalSearchRequest) {

        if (hospitalSearchRequest.getPage() == null || hospitalSearchRequest.getPage() < 0) {
            hospitalSearchRequest.initPage();
        }

        if (hospitalSearchRequest.getSize() == null || hospitalSearchRequest.getSize() < 0) {
            hospitalSearchRequest.initSize();
        }

        if(hospitalSearchRequest.getSize() > 15 && hospitalSearchRequest.getPage() > 45) {
            hospitalSearchRequest.initPage();
            hospitalSearchRequest.initSize();
        }

        KakaoCategorySearchResponse categorySearchResponse = kakaoHospitalApiClient.searchHospitals(
                CategoryGroupCode.HP8.name(),
                hospitalSearchRequest.getX(),
                hospitalSearchRequest.getY(),
                radius,
                hospitalSearchRequest.getPage(),
                hospitalSearchRequest.getSize(),
                distance
        );

        return HospitalSearchResponse.of(categorySearchResponse, hospitalSearchRequest.getPage(), hospitalSearchRequest.getSize());
    }
}