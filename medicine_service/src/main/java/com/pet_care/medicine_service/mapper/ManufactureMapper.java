package com.pet_care.medicine_service.mapper;

import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.model.Manufacture;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManufactureMapper {
    /**
     * @param manufacture
     * @return
     */
    ManufactureResponse toDto(Manufacture manufacture);
}
