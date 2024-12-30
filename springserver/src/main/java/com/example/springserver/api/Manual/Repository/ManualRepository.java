package com.example.springserver.api.Manual.Repository;

import com.example.springserver.api.Manual.Domain.Manual;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManualRepository extends JpaRepository<Manual, Long> {

    Optional<Manual> findByEmergencyName(String emergencyName);
    Optional<Manual> findByKeyword(String keyword);

    @Query("SELECT m.emergencyName FROM Manual m")
    List<String> findAllEmergencyNames();

    @Query("SELECT m FROM Manual m WHERE m.manualDetail LIKE %:keyword% or m.keyword LIKE %:keyword%")
    Optional<Manual> findByDetailContaining(String keyword);
}