package com.example.springserver.api.EmergencyMap.Repository;

import com.example.springserver.api.EmergencyMap.Domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {
    Optional<Hospital> findByPlaceId(String placeId);
}