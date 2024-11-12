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

    LocationRepository locationRepository;  // Repository to interact with the database for Location data
    LocationMapper locationMapper;  // Mapper to convert entities to DTOs

    /**
     * Retrieves all locations from the repository and maps them to LocationResponse DTOs.
     *
     * @return List of LocationResponse DTOs
     */
    public List<LocationResponse> getAllLocations() {
        // Fetches all locations from the repository, maps them to DTOs and collects them into a list
        List<LocationResponse> locationResponses = locationRepository.findAll()
                .stream().map(locationMapper::toDto).toList();

        // Logs the operation
        log.info("Get all locations");

        // Returns the list of LocationResponse DTOs
        return locationResponses;
    }

    /**
     * Retrieves a location by its ID. Throws an APIException if the location is not found.
     *
     * @param id The ID of the location to retrieve
     * @return The LocationResponse DTO of the found location
     */
    public LocationResponse getLocationById(Long id) {
        // Fetches the location by ID, throws an exception if not found, and maps it to a DTO
        LocationResponse locationResponse = locationMapper
                .toDto(locationRepository.findById(id)
                        .orElseThrow(() -> new APIException(ErrorCode.LOCATION_NOT_FOUND)));

        // Logs the operation
        log.info("Get location by id");

        // Returns the LocationResponse DTO
        return locationResponse;
    }
}

