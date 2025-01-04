package com.example.springserver.api.EmergencyMap.Domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "like_board")
public class LikeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    private Long memberId;


    private LikeBoard(String placeId, Long memberId) {
        this.placeId = placeId;
        this.memberId = memberId;
    }

    public static LikeBoard createLikeBoard(String placeId, Long memberId) {
        return new LikeBoard(placeId, memberId);
    }
}