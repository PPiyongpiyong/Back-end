package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;

public interface HospitalService {
    HospitalSearchResponse searchHospitals(String authToken, Integer page, Integer size, String x, String y, String categoryName);
}