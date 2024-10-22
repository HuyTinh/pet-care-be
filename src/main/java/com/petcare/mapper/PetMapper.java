package com.petcare.mapper;

import com.petcare.dto.response.PetResponse;
import com.petcare.entity.Pet;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PetMapper
{

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    @Mapping(source = "id", target = "petId")
    @Mapping(source = "name", target = "petName")
    @Mapping(source = "age", target = "petAge")
    @Mapping(source = "weight", target = "petWeight")
    @Mapping(source = "species.name", target = "petSpecies")
//    @Mapping(source = "owner.firstName", target = "ownerName")
    PetResponse mapperPetToPetResponse(Pet pet);
    List<PetResponse> mapperPetsToPetsResponse(List<Pet> pets);

    @AfterMapping()
    default void afterMappingPetsToPetsResponse(Pet pet, @MappingTarget PetResponse petResponse)
    {
        petResponse.setOwnerName(pet.getOwner().getLastName() + " " +  pet.getOwner().getFirstName());
    }

}
