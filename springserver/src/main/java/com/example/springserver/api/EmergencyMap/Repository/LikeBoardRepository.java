package com.example.springserver.api.EmergencyMap.Repository;

import com.example.springserver.api.EmergencyMap.Domain.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    LikeBoard findByMemberIdAndPlaceId(Long memberId, String placeId);
    List<LikeBoard> findByMemberId(Long memberId);
}
