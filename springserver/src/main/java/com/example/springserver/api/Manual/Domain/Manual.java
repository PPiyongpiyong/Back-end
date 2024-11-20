package com.example.springserver.api.Manual.Domain;

import jakarta.persistence.*;

@Entity
public class Manual {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  manualId;

    @Column(nullable = false)
    private String emergencyName;

    @Column(nullable = false)
    private String manualSummary;

    private String manualDetail;

    private String category;
}
