package com.example.springserver.api.Manual.Controller;

import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Domain.ManualFavorite;
import com.example.springserver.api.Manual.Dto.ManualCategory.ManualCategoryRespond.ManualCategoryRespondDto;
import com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond.ManualDetailRespondDto;
import com.example.springserver.api.Manual.Service.ManualService;
import com.example.springserver.external.S3Service;
import com.example.springserver.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com .example.springserver.global.exception.ErrorCode; // 실제 경로로 변경

import java.util.List;


@RestController
@RequestMapping("/api/v1/manual")

@RequiredArgsConstructor
@Tag(name = "Manual", description = "매뉴얼 관련 API")
public class ManualController {
    private final ManualService manualService;
    private final S3Service s3Service;


    @Operation(summary = "매뉴열 조회에 따른 매뉴얼 정보 반환", description =  """
            검색어에 따라 매뉴얼을 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {
            @Parameter(name = "emergencyName", description = "응급상황 이름", schema = @Schema(type = "string", example = "실신")),
            @Parameter(name = "keyword", description = "검색 키워드", schema = @Schema(type = "string", example = "심장"))
    })
    @GetMapping("/search")
    public Object searchManual(
            @RequestParam(required = false) String emergencyName,
            @RequestParam(required = false) String keyword) {
        // emergencyName이 제공된 경우
        if (emergencyName != null) {
            return manualService.getManualByEmergencyName(emergencyName);
        }
        // keyword가 제공된 경우
        if (keyword != null) {
            return manualService.getManualByEmergencyKeyword(keyword);
        }
        // 이 코드는 실행되지 않음 (정상적인 요청이라면)
        throw new CustomException(ErrorCode.INVALID_REQUEST);
    }



    @Operation(summary = "카테고리 별 조회", description = """
            카테고리 별로 매뉴얼을 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Category", description = "카테고리 이름", schema = @Schema(type = "string", example = "3.의학적"))})
    @GetMapping("/getCategory")
    public List<ManualCategoryRespondDto> searchCategory(
            @RequestParam String category) {
        return manualService.getManualByCategory(category);
    }

    @Operation(summary = "검색 자동완성", description = """
            검색과 일치하는 응금상황 이름들을 보여줍니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Keyword", description = "검색한 키워드", schema = @Schema(type = "string", example = "심장"))})
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(
            @RequestParam String keyword) {

        manualService.loadEmergencyNameIntoTrie();

        List<String> result = this.manualService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

    //즐겨찾기 추가
    @Operation(summary = "즐겨찾기 추가", description = """
        특정 사용자가 특정 응급상황을 즐겨찾기에 추가합니다.<br>
        """, parameters = {
            @Parameter(name = "memberId", description = "사용자 ID", schema = @Schema(type = "long", example = "1")),
            @Parameter(name = "manualId", description = "매뉴얼 ID", schema = @Schema(type = "long", example = "2"))
    })
    @PostMapping("/favorite")
    public ResponseEntity<ManualFavorite> addFavorite(@RequestParam Long memberId, @RequestParam Long manualId) {
        ManualFavorite manualFavorite = manualService.addFavorite(memberId, manualId);
        return ResponseEntity.ok(manualFavorite);
    }


    //즐겨찾기 삭제

    @Operation(summary = "즐겨찾기 삭제", description = """
        특정 사용자의 특정 응급상황 즐겨찾기를 삭제합니다.<br>
        """, parameters = {
            @Parameter(name = "memberId", description = "사용자 ID", schema = @Schema(type = "long", example = "1")),
            @Parameter(name = "manualId", description = "매뉴얼 ID", schema = @Schema(type = "long", example = "2"))
    })
    @DeleteMapping("/favorite")
    public ResponseEntity<String> deleteFavorite(@RequestParam Long memberId, @RequestParam Long manualId) {
        manualService.deleteFavorite(memberId, manualId);
        return ResponseEntity.ok("즐겨찾기가 삭제되었습니다.");
    }


    //즐겨찾기 조회
    @Operation(summary = "사용자별 즐겨찾기 조회", description = """
        특정 사용자의 즐겨찾기 목록을 조회합니다.<br>
        """, parameters = {
            @Parameter(name = "memberId", description = "사용자 ID", schema = @Schema(type = "long", example = "1"))
    })
    @GetMapping("/favorites")
    public ResponseEntity<List<ManualFavorite>> getFavorites(@RequestParam Long memberId) {
        List<ManualFavorite> favorites = manualService.getFavorites(memberId);
        System.out.println("favorites size: " + favorites.size());
        return ResponseEntity.ok(favorites);
    }



    @Operation(summary = "매뉴얼 세부내용 조회", description = """
            해당하는 응급상황 이름에 대한 대처 세부내용을 반환합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "EmergencyName", description = "응급상황 이름", schema = @Schema(type = "string", example = "심장마비"))})
    @GetMapping("/explanation")
    public ManualDetailRespondDto searchDetail(
            @RequestParam String emergencyName) {
        return manualService.getManualDetail(emergencyName);
    }





    /*@Operation(summary = "연관어 검색", description = """
            연관어를 통해 응급상황 매뉴얼을 검색하며, 검색과 일치하는 키워드가 있는 매뉴얼들을 반환합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Keyword", description = "검색할 내용", schema = @Schema(type = "string", example = "심장정지"))})
    @GetMapping("/keyword")
    public List<ManualKeywordRespond> getManualByEmergencyKeyword(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String authToken){

        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        return manualService.getManualByEmergencyKeyword(keyword, token);
    }*/

// 이미지 조회 base 64 이용
     /*@Operation(summary = "매뉴얼 검색", description = """
            응급상황 이름을 통해 매뉴얼을 검색합니다.<br>
            헤더에 accessToken을 넣어주세요.<br>
            """, parameters = {@Parameter(name = "Type", description = "이미지 확장자", schema = @Schema(type = "string", example = "jpg")),
                @Parameter(name = "EmergencyName", description = "응급상황 이름", schema = @Schema(type = "string", example = "심장마비"))})
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String emergencyName) {
        try {
            if (emergencyName != null) {
                // 매뉴얼 정보 조회
                ManualRespondDto manualRespondDto = manualService.getManualByEmergencyName(emergencyName);

                // 이미지 URL 조회
                String imageKey = emergencyName + ".jpeg";
                byte[] imageBytes = s3Service.getImage(imageKey);


                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // 매뉴얼 정보와 이미지 URL을 함께 반환
                HashMap<String, Object> response = new HashMap<>();
                response.put("manual", manualRespondDto);
                response.put("image", "data:image/jpeg;base64," + base64Image); // Send base64 image

                System.out.println("Manual: " + manualRespondDto);
                System.out.println("Base64 Image: " + base64Image);
                System.out.println("Response: " + response);

                return ResponseEntity.ok(response);
            } else {
                // emergencyName이 비어있거나 제공되지 않은 경우
                return ResponseEntity.badRequest().body("emergencyName 파라미터를 제공해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 스택 트레이스를 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 요청을 처리할 수 없습니다.");
        }

    }*/
}

