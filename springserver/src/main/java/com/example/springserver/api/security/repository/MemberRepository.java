package com.example.springserver.api.security.repository;

import com.example.springserver.api.security.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
    boolean existsByEmail(String email);
    Optional<MemberEntity> findByEmailAndProvider(String email, String provider);

    Optional<MemberEntity> findByEmail(String email);
}
