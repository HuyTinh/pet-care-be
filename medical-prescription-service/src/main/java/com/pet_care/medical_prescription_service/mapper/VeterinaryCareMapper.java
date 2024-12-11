package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.response.VeterinaryCareResponse;
import com.pet_care.medical_prescription_service.entity.VeterinaryCare;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface VeterinaryCareMapper {

    VeterinaryCareResponse toDto(VeterinaryCare veterinaryCare);
}