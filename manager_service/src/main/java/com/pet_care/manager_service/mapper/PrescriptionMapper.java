package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.PrescriptionResponse;
import com.pet_care.manager_service.entity.Prescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    PrescriptionResponse toPrescriptionResponse(Prescription prescription);
}
