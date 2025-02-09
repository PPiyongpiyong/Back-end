package com.example.springserver.api.EmergencyMap.Repository;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import com.example.springserver.api.EmergencyMap.Domain.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    Optional<LikeBoard> findByMemberIdAndHospital(Long memberId, Hospital hospital);
    List<LikeBoard> findByMemberId(Long memberId);
    List<LikeBoard> findByMemberIdAndHospitalIn(Long memberId, List<Hospital> hospital);
}