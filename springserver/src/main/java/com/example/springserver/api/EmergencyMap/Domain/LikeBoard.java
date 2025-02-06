package com.example.springserver.api.EmergencyMap.Domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "like_board")
public class LikeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "memberId")
    private Long memberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospital_id", referencedColumnName = "placeId") // 병원의 ID를 외래키로 설정
    private Hospital hospital;

    public LikeBoard(Long memberId, Hospital hospital) {
        this.memberId = memberId;
        this.hospital = hospital;
    }

    public static LikeBoard createLikeBoard(Long memberId, Hospital hospital) {
        return new LikeBoard(memberId, hospital);
    }
}