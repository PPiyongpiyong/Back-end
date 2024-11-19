package com.example.springserver.api.Manual.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Manual {
    @Id @GeneratedValue
    private long  manualId;

    private String emergencyName;

    private String manualSummary;

    private String manualDetail;

    private String category;
}
