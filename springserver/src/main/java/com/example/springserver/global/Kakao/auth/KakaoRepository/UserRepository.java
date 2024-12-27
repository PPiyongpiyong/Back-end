package com.example.springserver.global.Kakao.auth.KakaoRepository;

import com.example.springserver.global.Kakao.auth.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByPlatformId(String platformId);
    boolean existsUserByPlatformId(String platformId);
    boolean existsUserByNickname(String nickname);
}
