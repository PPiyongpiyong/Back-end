package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Service.HospitalService;
import com.example.springserver.api.EmergencyMap.Service.LikeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final LikeBoardService likeBoardService;

    // 병원 검색 API (기존 코드)
    @GetMapping("/hospital")
    public ResponseEntity<HospitalSearchResponse> getHospitals(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @RequestParam String x,
                                                               @RequestParam String y,
                                                               @RequestParam String categoryName,
                                                               @RequestParam Long userId) {
        return ResponseEntity.ok().body(hospitalService.searchHospitals(page, size, x, y, categoryName, userId));
    }


}
