package com.example.springserver.api.EmergencyMap.Dto;


import lombok.Getter;

@Getter
public class HospitalSearchMeta {
    private Integer pageableCount;
    private Boolean isEnd;

    public HospitalSearchMeta(Boolean isEnd, Integer pageableCount) {
        this.isEnd = isEnd;
        this.pageableCount = pageableCount;
    }
}