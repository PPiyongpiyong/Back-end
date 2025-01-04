package com.example.springserver.api.Mypage.repository;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
    boolean existsByEmail(String email);

    Optional<MemberEntity> findByEmail(String email);


}
