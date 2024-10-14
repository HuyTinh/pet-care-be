package com.pet_care.medicine_service.mapper;

import com.pet_care.medicine_service.dto.response.LocationResponse;
import com.pet_care.medicine_service.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {
    /**
     * @param location
     * @return
     */
    LocationResponse toDto(Location location);
}
