package com.example.springserver.api.Manual.Dto.ManualFavorite.ManualFavoriteRespond;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManualGetRespond {

    private String category;
    private String emergencyName;
    private String emergencyResponseSummary;
    private String emergencyImage;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyResponseSummary() {
        return emergencyResponseSummary;
    }

    public void setEmergencyResponseSummary(String emergencyResponseSummary) {
        this.emergencyResponseSummary = emergencyResponseSummary;
    }

    public String getEmergencyImage() {
        return emergencyImage;
    }

    public void setEmergencyImage(String emergencyImage) {
        this.emergencyImage = emergencyImage;
    }
}

