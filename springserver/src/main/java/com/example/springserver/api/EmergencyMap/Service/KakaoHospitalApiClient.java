package com.example.springserver.api.EmergencyMap.Service;


import com.example.springserver.api.EmergencyMap.config.KakaoFeignConfig;
import com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi.KakaoCategorySearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoHospitalApiClient", url = "https://dapi.kakao.com/", configuration = KakaoFeignConfig.class)
public interface KakaoHospitalApiClient {

    @GetMapping("v2/local/search/category.json")
    KakaoCategorySearchResponse searchHospitals(
            @RequestParam("category_group_code") String categoryGroupCode,
            @RequestParam("x") String x,
            @RequestParam("y") String y,
            @RequestParam("category_Name") String categoryName,
            @RequestParam("radius") int radius,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String distance
    );
}