package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Service.ManualService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manual")
@RequiredArgsConstructor
public class ManualController {
    private final ManualService manualService;

    @GetMapping("/search")
    public ManualRespondDto searchManual(@RequestParam String emergencyName) {
        // ManualService에서 반환된 ManualRespondDto를 그대로 반환
        return manualService.getManualByEmergencyName(emergencyName);
    }

    @GetMapping("/getCategory")
    public List<ManualCategoryRespondDto> searchCategory(@RequestParam String category) {
        return manualService.getManualByCategory(category);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String keyword) {

        manualService.loadEmergencyNameIntoTrie();

        List<String> result = this.manualService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/explanation")
    public ManualDetailRespondDto searchDetail(@RequestParam String emergencyName) {
        return manualService.getManualDetail(emergencyName);
    }

}

