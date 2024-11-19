package com.example.springserver.api.EmergencyMap.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hospitals {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospiId;

    private String hospitalName;

    private Double latitude;
    private Double longitude;

    private String address;

    private String hospiTel;
}
