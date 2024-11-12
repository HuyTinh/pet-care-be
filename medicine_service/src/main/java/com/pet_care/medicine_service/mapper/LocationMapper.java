package com.pet_care.medicine_service.mapper;

// Importing necessary libraries for mapping
import com.pet_care.medicine_service.dto.response.LocationResponse;
import com.pet_care.medicine_service.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    /**
     * Converts a Location entity to a LocationResponse DTO.
     *
     * @param location the Location entity to convert
     * @return the corresponding LocationResponse DTO
     */
    LocationResponse toDto(Location location);
}
