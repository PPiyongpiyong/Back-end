package com.example.springserver.api.EmergencyMap.Service;


import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchRequest;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
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

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public HospitalSearchResponse searchHospitals(Integer page, Integer size, String x, String y,
                                                  String token) {

        // 인증 토큰이 유효한지 확인
        MemberEntity member = memberRepository.findByMemberId(tokenProvider.getMemberIdFromToken(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

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