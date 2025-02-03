package com.example.springserver.api.Manual.Repository;


import com.example.springserver.api.Manual.Domain.Manual;
import com.example.springserver.api.Manual.Domain.ManualFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManualFavoriteRepository extends JpaRepository<ManualFavorite, Long> {
    Optional<ManualFavorite> findByEmergencyName(String emergencyName);
}

