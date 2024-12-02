package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.entity.PetPrescription;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetPrescriptionMapper {
    /**
     * @param petPrescriptionCreateRequest
     * @return
     */
    PetPrescription toEntity(PetPrescriptionCreateRequest petPrescriptionCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "petMedicines", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    PetPrescription partialUpdate(PetPrescriptionUpdateRequest petPrescriptionUpdateRequest, @MappingTarget PetPrescription petPrescription);

}
