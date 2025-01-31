package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@Tag(name = "EmergencyMap", description = "지도 관련 API")
public class HospitalController {

    private final HospitalService hospitalService;

    @Operation(summary = "근처 병원 조회", description = """
            위도와 경도를 기반으로 근처 병원 주소를 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """)
    @GetMapping("/hospital")
    public ResponseEntity<HospitalSearchResponse> getHospitals(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "15") Integer size,
                                                               @RequestParam String x,
                                                               @RequestParam String y,
                                                               @RequestParam String categoryName) {
        return ResponseEntity.ok().body(hospitalService.searchHospitals(page, size, x, y, categoryName));

    }
}