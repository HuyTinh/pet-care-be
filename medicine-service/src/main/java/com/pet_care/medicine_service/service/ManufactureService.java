package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.ManufactureMapper;
import com.pet_care.medicine_service.repository.ManufactureRepository;
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
public class ManufactureService {

    ManufactureRepository manufactureRepository;  // Repository to interact with the database for Manufacture data
    ManufactureMapper manufactureMapper;  // Mapper to convert entities to DTOs

    /**
     * Retrieves all manufacture records from the repository and maps them to ManufactureResponse DTOs.
     *
     * @return List of ManufactureResponse DTOs
     */
    public List<ManufactureResponse> getAllManufacture() {
        // Fetches all manufacture records from the repository, maps them to DTOs and collects them into a list
        List<ManufactureResponse> manufactureResponseList = manufactureRepository.findAll().stream().map(manufactureMapper::toDto).toList();

        // Logs the operation and the result
        log.info("Manufacture List: {}", manufactureResponseList);

        // Returns the list of ManufactureResponse DTOs
        return manufactureResponseList;
    }

    /**
     * Retrieves a manufacture record by its ID. Throws an APIException if the manufacture is not found.
     *
     * @param id The ID of the manufacture record to retrieve
     * @return The ManufactureResponse DTO of the found manufacture record
     */
    public ManufactureResponse getManufactureById(Long id) {
        // Fetches the manufacture record by ID, throws an exception if not found, and maps it to a DTO
        ManufactureResponse manufactureResponse = manufactureMapper.toDto(manufactureRepository.findById(id).orElseThrow(() -> new APIException(ErrorCode.MANUFACTURE_NOT_FOUND)));

        // Logs the operation and the result
        log.info("Manufacture Response: {}", manufactureResponse);

        // Returns the ManufactureResponse DTO
        return manufactureResponse;
    }
}
