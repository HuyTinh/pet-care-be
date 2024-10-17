package com.pet_care.medical_prescription_service.mapper;

import com.pet_care.medical_prescription_service.dto.request.PrescriptionDetailCreateRequest;
import com.pet_care.medical_prescription_service.model.PrescriptionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrescriptionDetailMapper {
    /**
     * @param createRequest
     * @return
     */
    PrescriptionDetail toEntity(PrescriptionDetailCreateRequest createRequest);
}