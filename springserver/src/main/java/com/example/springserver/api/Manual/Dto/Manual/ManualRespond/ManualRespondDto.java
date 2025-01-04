package com.example.springserver.api.Manual.Dto.Manual.ManualRespond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualRespondDto {
    private String emergencyName;
    private String emergencyResponseSummary;

}
