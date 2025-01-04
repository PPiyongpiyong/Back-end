package com.example.springserver.api.EmergencyMap.Dto;

import lombok.Getter;

@Getter
public class HospitalSearchRequest {
    private String x;
    private String y;
    private String categoryName;
    private Integer page;
    private Integer size;

    public HospitalSearchRequest(String x, String y, String categoryName) {
        this.x = x;
        this.y = y;
        this.categoryName=categoryName;
    }

    public void initPage() {
        this.page = 1;
    }

    public void initSize() {
        this.size = 10;
    }


}