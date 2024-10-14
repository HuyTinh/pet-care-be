package com.pet_care.medicine_service.mapper;

import com.pet_care.medicine_service.dto.response.CalculationUnitResponse;
import com.pet_care.medicine_service.model.CalculationUnit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalculationUnitMapper {
    /**
     * @param calculationUnit
     * @return
     */
    CalculationUnitResponse toDto(CalculationUnit calculationUnit);
}
