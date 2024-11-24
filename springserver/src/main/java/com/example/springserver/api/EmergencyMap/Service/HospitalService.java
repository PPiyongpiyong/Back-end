package com.example.springserver.api.EmergencyMap.Service;

import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchRequest;
import com.example.springserver.api.EmergencyMap.Dto.HospitalSearchResponse;

public interface HospitalService {
    HospitalSearchResponse searchHospitals(HospitalSearchRequest hospitalSearchRequest);
}