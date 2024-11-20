package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Dto.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Service.ManualService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manual")
@RequiredArgsConstructor
public class ManualController {
    private final ManualService manualService;

    @GetMapping("/search")
    public ManualRespondDto searchManual(@RequestParam String emergencyName) {
        // ManualService에서 반환된 ManualRespondDto를 그대로 반환
        return manualService.getManualByEmergencyName(emergencyName);
    }
}

