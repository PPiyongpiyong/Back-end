package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Dto.Manual.ManualRespond.ManualRespondDto;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Dto.ManualKeyword.ManualKeywordRespond.ManualKeywordRespond;
import com.example.springserver.api.Manual.Service.ManualService;
import com.example.springserver.external.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Manual", description = "매뉴얼 관련 API")
public class ManualController {
    private final ManualService manualService;
    private final S3Service s3Service;

    @Operation(summary = "매뉴얼 검색", description = """
            응급상황 이름을 통해 매뉴얼을 검색합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Type", description = "이미지 확장자", schema = @Schema(type = "string", example = "jpg")),
                @Parameter(name = "EmergencyName", description = "응급상황 이름", schema = @Schema(type = "string", example = "심장마비"))})
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


    @Operation(summary = "카테고리 별 조회", description = """
            카테고리 별로 매뉴얼을 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Category", description = "카테고리 이름", schema = @Schema(type = "string", example = "3.의학적"))})
    @GetMapping("/getCategory")
    public List<ManualCategoryRespondDto> searchCategory(
            @RequestParam String category,
            @RequestHeader("Authorization") String authToken) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualByCategory(category, token);
    }

    @Operation(summary = "검색 자동완성", description = """
            검색과 일치하는 응금상황 이름들을 보여줍니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Keyword", description = "검색한 키워드", schema = @Schema(type = "string", example = "심장"))})
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


    @Operation(summary = "매뉴얼 세부내용 조회", description = """
            해당하는 응급상황 이름에 대한 대처 세부내용을 반환합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "EmergencyName", description = "응급상황 이름", schema = @Schema(type = "string", example = "심장마비"))})
    @GetMapping("/explanation")
    public ManualDetailRespondDto searchDetail(
            @RequestParam String emergencyName,
            @RequestHeader String authToken) {

        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualDetail(emergencyName, token);
    }

    @Operation(summary = "연관어 검색", description = """
            연관어를 통해 응급상황 매뉴얼을 검색하며, 검색과 일치하는 키워드가 있는 매뉴얼들을 반환합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Keyword", description = "검색할 내용", schema = @Schema(type = "string", example = "심장정지"))})
    @GetMapping("/keyword")
    public List<ManualKeywordRespond> getManualByEmergencyKeyword(
            @RequestParam String keyword,
            @RequestHeader String authToken){

        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualByEmergencyKeyword(keyword, token);
    }
}

