package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final KakaoHospitalApiClient kakaoHospitalApiClient;
    private static final Integer radius = 10000;
    private static final String distance = "distance";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public HospitalSearchResponse searchHospitals(Integer page, Integer size, String x, String y, String categoryName, String token) {
        // token 인증 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));


        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException("size는 1~50 사이의 값이어야 합니다.");
        }

        // 카테고리 이름이 "진료과 선택"일 경우 필터링 없이 호출
        String effectiveCategoryName = "진료과 선택".equals(categoryName) ? null : categoryName;

        // Kakao API 호출
        KakaoCategorySearchResponse categorySearchResponse = kakaoHospitalApiClient.searchHospitals(
                CategoryGroupCode.HP8.name(),
                x,
                y,
                effectiveCategoryName,
                radius,
                page,
                size,
                distance
        );


        // 필터링 조건 수정: "진료과 선택"일 경우 필터링하지 않음
        // 필터링 조건 적용
        List<Document> filteredDocuments = categorySearchResponse.getDocuments();

        // "진료과 선택"이 아닌 경우에만 추가 필터링
        if (effectiveCategoryName != null) {
            filteredDocuments = filteredDocuments.stream()
                    .filter(document -> document.getCategoryName().contains(effectiveCategoryName))
                    .toList();
        }



        return HospitalSearchResponse.ofFiltered(categorySearchResponse, filteredDocuments);
        // 필터링된 데이터를 기반으로 응답 생성

    }
}
