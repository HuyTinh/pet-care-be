package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PetMedicineCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PetMedicineUpdateRequest;
import com.pet_care.medical_prescription_service.entity.PetMedicine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetMedicineMapper {
    /**
     * @param createRequest
     * @return
     */
    PetMedicine toEntity(PetMedicineCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "petPrescription", ignore = true)
    PetMedicine partialUpdate(PetMedicineUpdateRequest petMedicineUpdateRequest, @MappingTarget PetMedicine petPrescription);

}