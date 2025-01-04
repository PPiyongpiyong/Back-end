package com.example.springserver.api.EmergencyMap.Repository;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByPlaceId(String placeId);
}
