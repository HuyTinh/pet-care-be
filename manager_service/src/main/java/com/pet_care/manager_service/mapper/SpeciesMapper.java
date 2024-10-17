package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.SpeciesResponse;
import com.pet_care.manager_service.entity.Species;
import org.mapstruct.Mapper;

@Mapper
public interface SpeciesMapper {
    SpeciesResponse toResponse(Species species);
}
