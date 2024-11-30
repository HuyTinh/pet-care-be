package com.pet_care.medicine_service.mapper;

// Importing necessary libraries for mapping
import com.pet_care.medicine_service.dto.response.CalculationUnitResponse;
import com.pet_care.medicine_service.entity.CalculationUnit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalculationUnitMapper {

    /**
     * Converts a CalculationUnit entity to a CalculationUnitResponse DTO.
     *
     * @param calculationUnit the CalculationUnit entity to convert
     * @return the corresponding CalculationUnitResponse DTO
     */
    CalculationUnitResponse toDto(CalculationUnit calculationUnit);
}
