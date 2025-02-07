package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import com.example.springserver.api.EmergencyMap.Dto.ApiResponse;
import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSaveRequest;
import com.example.springserver.api.EmergencyMap.Service.LikeBoardService;
import com.example.springserver.api.EmergencyMap.Repository.HospitalRepository; // 추가
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hospitals")
@RequiredArgsConstructor
public class LikeBoardController {

    private final LikeBoardService likeBoardService;

    @Operation(summary = "근처 병원 좋아요", description = """
            좋아요할 병원정보 기반으로 좋아요를 추가합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """)
    // 좋아요 병원 등록
    @PostMapping("/like")
    public ResponseEntity<?> likeHospital(@RequestHeader("Authorization") String authToken,
                                          @RequestBody HospitalSaveRequest hospitalSaveRequest) {
        return likeBoardService.like(authToken, hospitalSaveRequest);
    }

    @Operation(summary = "근처 병원 좋아요 취소하기", description = """
            취소할 병원아이디 기반으로 좋아요를 취소합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """)
    @PostMapping("/unlike/{placeId}")
    public ResponseEntity<ApiResponse<?>> unlikeHospital(@RequestHeader("Authorization") String authToken,
                                                         @PathVariable("placeId") String placeId) {

        // 좋아요 취소 메서드 호출
        return likeBoardService.unlike(authToken, placeId);
    }
}