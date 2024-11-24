package com.example.springserver.api.EmergencyMap.Controller;

import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchRequest;
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

    @PostMapping("/getHospital")
    public ResponseEntity<HospitalSearchResponse> getHospitals(@RequestBody HospitalSearchRequest request) {
        return ResponseEntity.ok().body(hospitalService.searchHospitals(request));
    }
}