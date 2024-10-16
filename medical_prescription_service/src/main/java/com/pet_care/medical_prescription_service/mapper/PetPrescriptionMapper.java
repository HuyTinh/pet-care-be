package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.model.PetPrescription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetPrescriptionMapper {
    PetPrescription toEntity(PetPrescriptionCreateRequest petPrescriptionCreateRequest);
}
