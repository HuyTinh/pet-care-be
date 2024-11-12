package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.response.CalculationUnitResponse;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.CalculationUnitMapper;
import com.pet_care.medicine_service.repository.CalculationUnitRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitService {

    CalculationUnitRepository calculationUnitRepository;  // Repository to interact with the database for CalculationUnit data
    CalculationUnitMapper calculationUnitMapper;  // Mapper to convert entities to DTOs

    /**
     * Retrieves all calculation units from the repository and maps them to CalculationUnitResponse DTOs.
     *
     * @return List of CalculationUnitResponse DTOs
     */
    public List<CalculationUnitResponse> getAllCalculationUnit() {
        // Fetches all calculation units from the repository, maps them to DTOs and collects them into a list
        List<CalculationUnitResponse> calculationUnitResponses = calculationUnitRepository.findAll().stream().map(calculationUnitMapper::toDto).toList();

        // Logs the operation and the result
        log.info("Get all calculation unit responses: {}", calculationUnitResponses);

        // Returns the list of CalculationUnitResponse DTOs
        return calculationUnitResponses;
    }

    /**
     * Retrieves a list of calculation units by their IDs.
     * If the IDs do not exist in the repository, an empty list will be returned.
     *
     * @param calculationUnitsInIds A set of IDs for the calculation units to retrieve
     * @return List of CalculationUnitResponse DTOs for the given IDs
     */
    public List<CalculationUnitResponse> getCalculationUnitsInIds(Set<Long> calculationUnitsInIds) {
        // Fetches calculation units by the provided set of IDs, maps them to DTOs and collects them into a list
        List<CalculationUnitResponse> calculationUnitResponses = calculationUnitRepository.findAllById(calculationUnitsInIds).stream().map(calculationUnitMapper::toDto).toList();

        // Logs the operation and the provided IDs
        log.info("Find calculation by ids: {}", calculationUnitsInIds);

        // Returns the list of CalculationUnitResponse DTOs for the provided IDs
        return calculationUnitResponses;
    }

    /**
     * Retrieves a calculation unit by its ID. Throws an APIException if the calculation unit is not found.
     *
     * @param id The ID of the calculation unit to retrieve
     * @return The CalculationUnitResponse DTO of the found calculation unit
     */
    public CalculationUnitResponse getCalculationUnitById(Long id) {
        // Fetches the calculation unit by ID, throws an exception if not found, and maps it to a DTO
        CalculationUnitResponse calculationUnitResponse = calculationUnitMapper.toDto(calculationUnitRepository.findById(id).orElseThrow(() -> new APIException(ErrorCode.CALCULATION_UNIT_NOT_FOUND)));

        // Logs the operation and the result
        log.info("Get calculation unit response: {}", calculationUnitResponse);

        // Returns the CalculationUnitResponse DTO
        return calculationUnitResponse;
    }
}
