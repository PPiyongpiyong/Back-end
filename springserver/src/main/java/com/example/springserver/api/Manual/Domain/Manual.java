package com.example.springserver.api.Manual.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 자동으로 생성 (new Bank())
@AllArgsConstructor // 모든 필드를 매개변수로 받는 전체 생성자를 자동으로 생성
@Builder
@Entity
@Table(name = "manual")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Manual {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  manual_id;

    @Column(nullable = false)
    private String emergencyName;

    @Column(nullable = false)
    private String manualSummary;

    @Lob
    private String manualDetail;

    private String category;

    private String keyword;

    private String imgurl;

    //코드리뷰 반영 OneToMany
    @OneToMany(mappedBy = "manual", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ManualFavorite> manualFavorites = new ArrayList<>();
}
