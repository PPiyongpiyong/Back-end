package com.example.springserver.api.EmergencyMap.Dto;

import lombok.Getter;

@Getter
public class HospitalSearchRequest {
    private String x;
    private String y;
    private Integer page;
    private Integer size;

    public HospitalSearchRequest(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public void initPage() {
        this.page = 1;
    }

    public void initSize() {
        this.size = 10;
    }


}