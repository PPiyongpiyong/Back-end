package com.example.springserver.api.Manual.Domain;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "manual_favorite")
public class ManualFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 사용자와 N:1 관계
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member; // 즐겨찾기한 사용자 정보

    @ManyToOne(fetch = FetchType.LAZY) // 매뉴얼과 N:1 관계
    @JoinColumn(name = "manual_id", nullable = false)
    private Manual manual; // 즐겨찾기한 매뉴얼 정보

    public ManualFavorite(MemberEntity member, Manual manual) {
        this.member = member;
        this.manual = manual;
    }
}
