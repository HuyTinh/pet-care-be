package com.pet_care.medicine_service.mapper;

// Importing necessary libraries for mapping
import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.model.Manufacture;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManufactureMapper {

    /**
     * Converts a Manufacture entity to a ManufactureResponse DTO.
     *
     * @param manufacture the Manufacture entity to convert
     * @return the corresponding ManufactureResponse DTO
     */
    ManufactureResponse toDto(Manufacture manufacture);
}
