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

    @Column(nullable = false) // 🔹 추가 필드로 사용자의 이메일 저장
    private String email;

    @Column(nullable = false) // 🔹 추가 필드로 매뉴얼의 응급상황이름 저장
    private String emergencyName;

    private String category;
    private String emergencyResponseSummary;
    private String imgurl;
    @Column(columnDefinition = "TEXT")
    private String keyword;
    @Column(columnDefinition = "TEXT")
    private String manualDetail;
    private String manualSummary;

    public ManualFavorite(MemberEntity member, Manual manual) {
        this.member = member;
        this.manual = manual;
        this.email = member.getEmail(); // 추가 필드에 이메일 저장
        this.emergencyName = manual.getEmergencyName();
        this.category = manual.getCategory();
        this.emergencyResponseSummary = manual.getManualSummary();
        this.imgurl = manual.getImgurl();
        this.keyword = manual.getKeyword();
        this.manualDetail = manual.getManualDetail();
        this.manualSummary = manual.getManualSummary();
        // 추가 필드에 응급상황이름 저장
    }
}
