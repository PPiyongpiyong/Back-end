package com.example.springserver.api.Manual.Repository;

import com.example.springserver.api.Manual.Domain.Manual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManualRepository extends JpaRepository<Manual, Long> {
    Optional<Manual> findByEmergencyName(String emergencyName);
}