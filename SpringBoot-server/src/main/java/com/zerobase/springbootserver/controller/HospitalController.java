package com.zerobase.springbootserver.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hospital")
public class HospitalController {

    // 병원 정보 반환 API
    @GetMapping("/getHospital")
    public ResponseEntity<Page<String>> getHospital() {

    }
}
