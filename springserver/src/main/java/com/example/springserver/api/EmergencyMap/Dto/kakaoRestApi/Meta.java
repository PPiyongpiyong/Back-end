package com.example.springserver.api.EmergencyMap.Dto.kakaoRestApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Meta {

    @JsonProperty("same_name")
    private SameName sameName;

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("pageable_count")
    private Integer pageableCount;

    @JsonProperty("is_end")
    private Boolean isEnd;
}