package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;
import com.example.springserver.api.EmergencyMap.Service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/hospital")
    public ResponseEntity<HospitalSearchResponse> getHospitals(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                                               @RequestParam String x,
                                                               @RequestParam String y) {
        return ResponseEntity.ok().body(hospitalService.searchHospitals(page, size, x, y));
    }
}