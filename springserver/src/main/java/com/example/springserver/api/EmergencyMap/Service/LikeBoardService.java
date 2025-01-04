package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import com.example.springserver.api.EmergencyMap.Domain.LikeBoard;
import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSaveRequest;
import com.example.springserver.api.EmergencyMap.Repository.HospitalRepository;
import com.example.springserver.api.EmergencyMap.Repository.LikeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeBoardService {

    private final LikeBoardRepository likeBoardRepository;
    private final HospitalRepository hospitalRepository;

    // 좋아요 병원 등록
    public void likeHospital(Long memberId, HospitalSaveRequest request) {


        request.validateFields();

        Hospital hospital = Hospital.createHospital(
                request.getPlaceId(),
                request.getPlaceName(),
                request.getAddressName(),
                request.getRoadAddressName(),
                request.getCategoryName(),
                request.getPhone(),
                request.getX(),
                request.getY()
        );

        Hospital savedHospital = hospitalRepository.findByPlaceId(request.getPlaceId())
                .orElseGet(() -> hospitalRepository.save(hospital));

        /**
         * 좋아요가 안된 병원 정보일때 좋아요를 등록하는 경우 좋아요 정보를 생성하고 저장
         * 좋아요가 된 병원 정보일 때 좋아요 정보를 등록하는 경우 throw 예외
         */
        LikeBoard likeBoard = likeBoardRepository.findByMemberIdAndPlaceId(memberId, savedHospital.getPlaceId());

        if(likeBoard != null) {
            throw new IllegalArgumentException("이미 즐겨찾기 한 병원입니다.");
        }

        LikeBoard createLikeBoard = LikeBoard.createLikeBoard(savedHospital.getPlaceId(), memberId);
        likeBoardRepository.save(createLikeBoard);
    }


    // 좋아요 취소
    public void unlikeHospital(Long memberId, String placeId) {
        LikeBoard likeBoard = likeBoardRepository.findByMemberIdAndPlaceId(memberId, placeId);

        if(likeBoard == null) {
            throw new IllegalArgumentException("즐겨찾기 한 병원이 아닙니다.");
        }

        likeBoardRepository.delete(likeBoard);
    }

    // 좋아요 병원 조회
    public List<HospitalInfo> getLikedHospitals(Long memberId) {

        return likeBoardRepository.findByMemberId(memberId).stream().map(
                likeBoard -> {
                    Hospital hospital = hospitalRepository.findByPlaceId(likeBoard.getPlaceId()).orElseThrow(
                            () -> new IllegalArgumentException("즐겨찾기 한 병원 정보가 없습니다.")
                    );
                    return HospitalInfo.createLikedHospitals(hospital);
                }
        ).collect(Collectors.toList());
    }
}
