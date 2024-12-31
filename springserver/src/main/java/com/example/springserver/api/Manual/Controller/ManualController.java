package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond.ManualKeywordRespond;
import com.example.springserver.api.Manual.Service.ManualService;
import com.example.springserver.external.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/v1/manual")
@RequiredArgsConstructor
public class ManualController {
    private final ManualService manualService;
    private final S3Service s3Service;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String type, @RequestParam(required = false) String emergencyName) {
        try {
            if ("manual".equals(type) && emergencyName != null) {
                // 매뉴얼 정보를 반환//
                ManualRespondDto manualRespondDto = manualService.getManualByEmergencyName(emergencyName);
                return ResponseEntity.ok(manualRespondDto);
            } else if ("image".equals(type) && emergencyName != null) {
                // S3 버킷에서 비상 상황 이름으로 이미지 파일 URL을 반환
                String imageKey = emergencyName + ".jpg";
                String imageUrl = Arrays.toString(s3Service.getImage(imageKey));
                return ResponseEntity.ok(imageUrl);
            } else if ("all".equals(type) && emergencyName != null) {
                // 매뉴얼 정보와 이미지 URL을 모두 반환
                ManualRespondDto manualRespondDto = manualService.getManualByEmergencyName(emergencyName);
                String imageKey = emergencyName + ".jpg";
                String imageUrl = Arrays.toString(s3Service.getImage(imageKey));
                HashMap<String, Object> response = new HashMap<>();
                response.put("manual", manualRespondDto);
                response.put("image", imageUrl);
                return ResponseEntity.ok(response);
            } else {
                // 필요한 파라미터가 누락되었거나 잘못된 요청 타입
                return ResponseEntity.badRequest().body("적절한 요청 파라미터를 제공해주세요.");
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 요청을 처리할 수 없습니다.");
        }
    }



    @GetMapping("/getCategory")
    public List<ManualCategoryRespondDto> searchCategory(
            @RequestParam String category,
            @RequestHeader("Authorization") String authToken) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualByCategory(category, token);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(
            @RequestParam String keyword,
            @RequestHeader String authToken) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        manualService.loadEmergencyNameIntoTrie(token);

        List<String> result = this.manualService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/explanation")
    public ManualDetailRespondDto searchDetail(
            @RequestParam String emergencyName,
            @RequestHeader String authToken) {

        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualDetail(emergencyName, token);
    }

    @GetMapping("/keyword")
    public ManualKeywordRespond getManualByEmergencyKeyword(
            @RequestParam String keyword,
            @RequestHeader String authToken){

        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualByEmergencyKeyword(keyword, token);
    }

}

