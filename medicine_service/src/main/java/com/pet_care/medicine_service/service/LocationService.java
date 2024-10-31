package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.response.LocationResponse;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.LocationMapper;
import com.pet_care.medicine_service.repository.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationService {
    LocationRepository locationRepository;

    LocationMapper locationMapper;

    /**
     * @return
     */
    public List<LocationResponse> getAllLocations() {
        List<LocationResponse> locationResponses = locationRepository.findAll()
                .stream().map(locationMapper::toDto).toList();

        log.info("Get all locations");

        return locationResponses;
    }

    /**
     * @param id
     * @return
     */
    public LocationResponse getLocationById(Long id) {
        LocationResponse locationResponse = locationMapper
                .toDto(locationRepository.findById(id)
                        .orElseThrow(() -> new APIException(ErrorCode.LOCATION_NOT_FOUND)));

        log.info("Get location by id");

        return locationResponse;
    }
}
