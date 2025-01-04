package com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRespond;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualDetailRespondDto {

    private String emergencyName;
    private String manualDetail;


}

