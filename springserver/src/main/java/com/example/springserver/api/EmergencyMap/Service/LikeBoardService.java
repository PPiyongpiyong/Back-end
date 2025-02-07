package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import com.example.springserver.api.EmergencyMap.Domain.LikeBoard;
import com.example.springserver.api.EmergencyMap.Dto.ApiResponse;
import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSaveRequest;
import com.example.springserver.api.EmergencyMap.Repository.HospitalRepository;
import com.example.springserver.api.EmergencyMap.Repository.LikeBoardRepository;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeBoardService {

    private final LikeBoardRepository likeBoardRepository;
    private final HospitalRepository hospitalRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    /**
     * 병원 좋아요
     * @param authToken
     * @param request
     * @return
     */

    @Transactional
    public ResponseEntity<ApiResponse<?>> like(String authToken,HospitalSaveRequest request) {
        /**
         * 병원 request 객체 필드 검증
         */
        request.validateFields();

        Long memberId = tokenProvider.getMemberIdFromToken(authToken);

        // Hospital 객체 생성
        Hospital hospitalEntity = Hospital.createHospital(
                request.getPlaceId(),
                request.getPlaceName(),
                request.getAddressName(),
                request.getRoadAddressName(),
                request.getCategoryName(),
                request.getPhone(),
                request.getX(),
                request.getY()
        );

        /**
         * 저장된 병원을 반환하고 없으면 새로운 병원을 저장하고 반환
         */
        Hospital savedHospital = hospitalRepository.findByPlaceId(request.getPlaceId())
                .orElseGet(() -> hospitalRepository.save(hospitalEntity));

        /**
         * 이미 즐겨찾기한 병원인지 확인
         */
        if (likeBoardRepository.findByMemberIdAndHospital(memberId, savedHospital).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "이미 즐겨찾기 한 병원입니다.", null));
        }

        /**
         * member 와 hospital 을 likeBoard 에 저장
         */
        LikeBoard likeBoard = LikeBoard.createLikeBoard(memberId, savedHospital);
        likeBoardRepository.save(likeBoard);

        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK.value(), "즐겨찾기에 추가되었습니다.", null));
    }

    // 좋아요 취소
    @Transactional
    public ResponseEntity<ApiResponse<?>> unlike(String authToken, String placeId) {

        // 병원 ID로 병원을 조회
        Hospital hospital = hospitalRepository.findByPlaceId(placeId).orElseThrow(
                () -> new IllegalArgumentException("해당 병원을 찾을 수 없습니다."));

        Long memberId = tokenProvider.getMemberIdFromToken(authToken);

        LikeBoard likeBoard = likeBoardRepository.findByMemberIdAndHospital(memberId, hospital)
                .orElseThrow(() -> new IllegalArgumentException("해당 병원에 대한 좋아요가 존재하지 않습니다."));


        likeBoardRepository.delete(likeBoard);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "즐겨찾기에서 병원이 취소되었습니다.", null));
    }


    // 좋아요한 병원 조회
    public ResponseEntity<ApiResponse<List<HospitalInfo>>> getLikedHospitals(String authToken) {

        Long memberId = tokenProvider.getMemberIdFromToken(authToken);

        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        /**
         * database 에 저장된 liked 병원 리스트 받아오기
         */
        List<LikeBoard> likedBoards = likeBoardRepository.findByMemberId(member.getMemberId());

        /**
         * global exception 으로    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT)); 처럼 처리하는게 좋아요
         */
        if (likedBoards.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "즐겨찾기한 병원이 없습니다.", List.of()));
        }

        List<HospitalInfo> likedHospitals = likedBoards.stream()
                .map(likeBoard -> {
                    Hospital hospital = likeBoard.getHospital();

                    hospital = hospitalRepository.findByPlaceId(hospital.getPlaceId())
                            .orElseThrow(() -> new IllegalArgumentException("❌ 즐겨찾기한 병원 정보를 찾을 수 없습니다."));

                    log.info("🏥 좋아하는 병원 정보: {}", hospital.getPlaceName());

                    return HospitalInfo.createLikedHospitals(hospital, true);
                })
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "좋아하는 병원 목록입니다.", likedHospitals));
    }





}