package com.example.springserver.api.security.OpenFeign.dto.respond;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class UserReissueRequestDto {
    private Long userId;


    public UserReissueRequestDto(Long userId) {
        this.userId = userId;
    }

    public Long userId() {
        return this.userId;
    }


}
