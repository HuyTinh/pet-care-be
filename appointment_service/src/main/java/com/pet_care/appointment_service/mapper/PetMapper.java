package com.pet_care.appointment_service.mapper;

import com.pet_care.appointment_service.dto.request.PetCreateRequest;
import com.pet_care.appointment_service.dto.response.PetResponse;
import com.pet_care.appointment_service.entity.Pet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetMapper {

    // Mapping from PetCreateRequest DTO to Pet entity
    Pet toEntity(PetCreateRequest request);

    // Mapping from Pet entity to PetResponse DTO
    PetResponse toDto(Pet pet);

    // Partial update for Pet entity using PetResponse DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Pet partialUpdate(PetResponse petResponse, @MappingTarget Pet pet);
}
