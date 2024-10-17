package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.PresriptionDetailResponse;
import com.pet_care.manager_service.entity.Prescription;
import com.pet_care.manager_service.entity.Prescription_Details;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionDetailMapper {
    PresriptionDetailResponse toPrescriptionDetailResponse(Prescription_Details prescription_Details);
}
