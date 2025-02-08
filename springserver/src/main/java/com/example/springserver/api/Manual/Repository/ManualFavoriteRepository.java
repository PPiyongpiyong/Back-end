package com.example.springserver.api.Manual.Repository;


import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Domain.ManualFavorite;
import com.example.springserver.api.Mypage.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManualFavoriteRepository extends JpaRepository<ManualFavorite, Long> {
    Optional<ManualFavorite> findByMemberAndManual(MemberEntity member, Manual manual);
    List<ManualFavorite> findByMember(MemberEntity member);
    void deleteByMemberAndManual(MemberEntity member, Manual manual);
}

