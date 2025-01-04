package com.example.springserver.api.Manual.Dto.ManualDetail.ManualDetailRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ManualDetailRequestDto {

    private String emergencyName;

}