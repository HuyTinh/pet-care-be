package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PetVeterinaryCareCreateRequest;
import com.pet_care.medical_prescription_service.entity.PetVeterinaryCare;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetVeterinaryCareMapper {
    PetVeterinaryCare toEntity(PetVeterinaryCareCreateRequest petVeterinaryCareCreateRequest);
}
