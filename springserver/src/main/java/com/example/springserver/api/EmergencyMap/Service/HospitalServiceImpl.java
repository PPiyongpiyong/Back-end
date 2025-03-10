package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import com.example.springserver.api.EmergencyMap.Repository.HospitalRepository;
import com.example.springserver.api.EmergencyMap.Repository.LikeBoardRepository;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final KakaoHospitalApiClient kakaoHospitalApiClient;
    private static final Integer radius = 10000;
    private static final String distance = "distance";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final HospitalRepository hospitalRepository;
    private final LikeBoardRepository likeBoardRepository;

    @Override
    public HospitalSearchResponse searchHospitals(String authToken, Integer page, Integer size, String x, String y, String categoryName) {

        if (size <= 0 || size > 25) {
            throw new IllegalArgumentException("size는 1~25 사이의 값이어야 합니다.");
        }

        // memberId를 Optional로 관리 (authToken이 없을 경우 null)
        final Long memberId = (authToken != null) ? tokenProvider.getMemberIdFromToken(authToken) : null;

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
        List<Document> filteredDocuments = categorySearchResponse.getDocuments().stream().peek(
                document -> {
                    if (memberId != null) { // Check if memberId is not null before setting favorite
                        Optional<Hospital> optionalHospital = hospitalRepository.findByPlaceId(document.getId());

                        optionalHospital.ifPresent(hospital -> {
                            if (likeBoardRepository.findByMemberIdAndHospital(memberId, hospital).isPresent()) {
                                document.favorite();
                            }
                        });
                    }
                }
        ).toList();

        // "진료과 선택"이 아닌 경우에만 추가 필터링
        if (effectiveCategoryName != null && !effectiveCategoryName.equals("진료과선택")) {
            filteredDocuments = filteredDocuments.stream()
                    .filter(document -> document.getCategoryName().contains(effectiveCategoryName))
                    .toList();
        }

        // 비회원일 경우, 즐겨찾기 기능은 지원하지 않으므로 메시지 전달
        return HospitalSearchResponse.ofFiltered(categorySearchResponse, filteredDocuments);
    }

}