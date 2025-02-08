package com.example.springserver.api.Manual.Dto.ManualFavorite.ManualFavoriteRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualPostRequest {
    private String emergencyName;
    private String email;
}
