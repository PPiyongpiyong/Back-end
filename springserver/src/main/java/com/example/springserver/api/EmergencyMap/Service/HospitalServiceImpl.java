package com.example.springserver.api.EmergencyMap.Service;


import com.example.springserver.api.EmergencyMap.Domain.CategoryGroupCode;
import com.example.springserver.api.EmergencyMap.Domain.LikeBoard;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.Document;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import com.example.springserver.api.EmergencyMap.Repository.LikeBoardRepository;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * hospital 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final KakaoHospitalApiClient kakaoHospitalApiClient;
    private final MemberRepository memberRepository;
    private final LikeBoardRepository likeBoardRepository;

    private static final Integer radius = 5000;
    private static final String distance = "distance";


    @Override
    public HospitalSearchResponse searchHospitals(Integer page, Integer size, String x, String y, String categoryName, Long memberId) {

        if (size <= 0 || size > 15) {
            throw new IllegalArgumentException("size는 1~15 사이의 값이어야 합니다.");
        }

        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 사용자가 없습니다.")
        );

        KakaoCategorySearchResponse categorySearchResponse = kakaoHospitalApiClient.searchHospitals(
                CategoryGroupCode.HP8.name(),
                x,
                y,
                radius,
                categoryName,
                page,
                size,
                distance
        );

        List<Document> filteredDocuments = categorySearchResponse.getDocuments().stream()
                .filter(document -> document.getCategoryName().contains(categoryName))
                .map(document -> {
                    LikeBoard likeBoard = likeBoardRepository.findByMemberIdAndPlaceId(member.getMemberId(), document.getId());
                    if (likeBoard != null) {
                        document.addToFavorites();
                    }
                    return document;
                })
                .toList();

        return HospitalSearchResponse.ofFiltered(categorySearchResponse, filteredDocuments);
    }
}