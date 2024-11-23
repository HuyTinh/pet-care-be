package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.response.PetVeterinaryCareResponse;
import com.pet_care.medical_prescription_service.model.PetVeterinaryCare;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetVeterinaryCareMapper {
    PetVeterinaryCareResponse toDto(PetVeterinaryCare petVeterinaryCare);
}
