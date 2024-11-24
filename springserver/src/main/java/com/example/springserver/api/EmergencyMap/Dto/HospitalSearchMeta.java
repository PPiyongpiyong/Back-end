package com.example.springserver.api.EmergencyMap.Dto;


import lombok.Getter;

@Getter
public class HospitalSearchMeta {
    private Integer totalCount;
    private Integer pageableCount;
    private Boolean isEnd;

    public HospitalSearchMeta(Boolean isEnd, Integer totalCount, Integer pageableCount) {
        this.isEnd = isEnd;
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
    }
}