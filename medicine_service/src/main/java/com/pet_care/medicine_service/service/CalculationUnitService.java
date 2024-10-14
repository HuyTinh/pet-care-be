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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitService {

    CalculationUnitRepository calculationUnitRepository;
    private final CalculationUnitMapper calculationUnitMapper;

    /**
     * @return
     */
    public List<CalculationUnitResponse> getAllCalculationUnit() {
        List<CalculationUnitResponse> calculationUnitResponses = calculationUnitRepository.findAll().stream().map(calculationUnitMapper::toDto).toList();
        log.info("Get all calculation unit responses: {}", calculationUnitResponses);
        return calculationUnitResponses;
    }

    /**
     * @param id
     * @return
     */
    public CalculationUnitResponse getCalculationUnitById(Long id) {
        CalculationUnitResponse calculationUnitResponse = calculationUnitMapper.toDto(calculationUnitRepository.findById(id).orElseThrow(() -> new APIException(ErrorCode.CALCULATION_UNIT_NOT_FOUND)));

        log.info("Get calculation unit response: {}", calculationUnitResponse);

        return calculationUnitResponse;
    }
}
