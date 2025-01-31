package com.example.springserver.api.Manual.Domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 자동으로 생성 (new Bank())
@AllArgsConstructor // 모든 필드를 매개변수로 받는 전체 생성자를 자동으로 생성
@Builder
@Entity
@Table(name = "manaul")
public class Manual {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  manualId;

    @Column(nullable = false)
    private String emergencyName;

    @Column(nullable = false)
    private String manualSummary;

    @Lob
    private String manualDetail;

    private String category;

    private String keyword;

    private String imgurl;

}
