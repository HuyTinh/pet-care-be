package com.pet_care.appointment_service.mapper;

import com.pet_care.appointment_service.dto.request.PetCreateRequest;
import com.pet_care.appointment_service.dto.response.PetResponse;
import com.pet_care.appointment_service.model.Pet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetMapper {
    Pet toEntity(PetCreateRequest request);

    PetResponse toDto(Pet pet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Pet partialUpdate(PetResponse petResponse, @MappingTarget Pet pet);
}