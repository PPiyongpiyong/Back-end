package com.example.springserver.api.security.repository;

import com.example.springserver.api.security.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findById(String id);

    boolean existsById(String id);

}
