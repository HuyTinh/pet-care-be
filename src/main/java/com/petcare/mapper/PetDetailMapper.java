package com.petcare.mapper;

import com.petcare.dto.response.PetDetailResponse;
import com.petcare.dto.response.PetResponse;
import com.petcare.dto.response.PrescriptionResponse;
import com.petcare.entity.Pet;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(uses = PrescriptionMapper.class)
public interface PetDetailMapper
{

    PetDetailMapper INSTANCE = Mappers.getMapper(PetDetailMapper.class);

    @Mapping(source = "id", target = "petId")
    @Mapping(source = "name", target = "petName")
    @Mapping(source = "age", target = "petAge")
    @Mapping(source = "weight", target = "petWeight")
    @Mapping(source = "species.name", target = "petSpecies")
    PetDetailResponse mapperPetToPetDetailResponse(Pet pet);
//    PetDetailResponse mapperPetsToPetDetailsResponse(Pet pets);

    @AfterMapping()
    default void afterMappingPetsToPetsResponse(Pet pet, @MappingTarget PetDetailResponse petResponse)
    {
        petResponse.setOwnerName(pet.getOwner().getLastName() + " " +  pet.getOwner().getFirstName());
        List<PrescriptionResponse> prescriptionResponses = PrescriptionMapper.INSTANCE.mapPrescriptionToPrescriptionResponse(pet.getPrescriptions());

        // Tính tổng totalPrice từ PrescriptionResponse
        double totalPrice = prescriptionResponses.stream()
                .mapToDouble(PrescriptionResponse::getTotalPriceInPrescription)  // Lấy trường totalPrice từ PrescriptionResponse
                .sum();

        // Gán giá trị tổng vào PetResponse
        petResponse.setTotalPriceInPetDetail(totalPrice);
        petResponse.setPrescriptionResponses(prescriptionResponses);
    }

}
