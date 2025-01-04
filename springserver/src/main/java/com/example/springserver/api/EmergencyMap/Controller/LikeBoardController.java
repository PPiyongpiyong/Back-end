package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSaveRequest;
import com.example.springserver.api.EmergencyMap.Service.LikeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeBoardController {

    private final LikeBoardService likeBoardService;

    // 좋아요 병원등록
    @PostMapping("/{memberId}")
    public void likeHospital(@PathVariable Long memberId, @RequestBody HospitalSaveRequest hospitalSaveRequest) {
        likeBoardService.likeHospital(memberId, hospitalSaveRequest);
    }


    // 좋아요 취소
    @PostMapping("/{memberId}/unlike/{placeId}")
    public void unlikeHospital(@PathVariable Long memberId, @PathVariable String placeId) {
        likeBoardService.unlikeHospital(memberId, placeId);
    }

    // 좋아요 병원 조회
    @GetMapping("/{memberId}/liked")
    public List<HospitalInfo> getLikedHospitals(@PathVariable Long memberId) {
        return likeBoardService.getLikedHospitals(memberId);
    }
}