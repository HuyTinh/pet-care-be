package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.model.Prescription;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrescriptionMapper {

    /**
     * @param prescription
     * @return
     */
    Prescription toEntity(PrescriptionCreateRequest prescription);

    /**
     * @param prescription
     * @return
     */
    PrescriptionResponse toResponse(Prescription prescription);
}
