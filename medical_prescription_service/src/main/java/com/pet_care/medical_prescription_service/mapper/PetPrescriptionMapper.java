package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.PetPrescriptionResponse;
import com.pet_care.medical_prescription_service.dto.response.PetVeterinaryCareResponse;
import com.pet_care.medical_prescription_service.entity.PetPrescription;
import com.pet_care.medical_prescription_service.entity.PetVeterinaryCare;
import com.pet_care.medical_prescription_service.repository.PetPrescriptionRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetPrescriptionMapper {

    PetPrescriptionResponse toDto(PetPrescription petPrescription);

    /**
     * @param petPrescriptionCreateRequest
     * @return
     */

    @Mapping(target = "petMedicines", ignore = true)
    @Mapping(target = "petVeterinaryCares", ignore = true)
    PetPrescription toEntity(PetPrescriptionCreateRequest petPrescriptionCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "petMedicines", ignore = true)
    @Mapping(target = "petVeterinaryCares", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    PetPrescription partialUpdate(PetPrescriptionUpdateRequest petPrescriptionUpdateRequest, @MappingTarget PetPrescription petPrescription);

}
