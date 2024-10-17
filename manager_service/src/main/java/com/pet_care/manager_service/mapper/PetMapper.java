package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.PetResponse;
import com.pet_care.manager_service.entity.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {

    PetResponse toPetResponse(Pet pet);
}
