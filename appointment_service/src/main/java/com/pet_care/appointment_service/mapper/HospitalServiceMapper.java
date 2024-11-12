package com.pet_care.appointment_service.mapper;

import com.pet_care.appointment_service.dto.request.HospitalServiceRequest;
import com.pet_care.appointment_service.dto.response.HospitalServiceResponse;
import com.pet_care.appointment_service.model.HospitalServiceEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HospitalServiceMapper {

    // Mapping from HospitalServiceRequest DTO to HospitalServiceEntity entity
    HospitalServiceEntity toEntity(HospitalServiceRequest hospitalServiceRequest);

    // Mapping from HospitalServiceEntity entity to HospitalServiceResponse DTO
    HospitalServiceResponse toDto(HospitalServiceEntity hospitalServiceEntity);

    // Partial update for HospitalServiceEntity using HospitalServiceRequest DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    HospitalServiceEntity partialUpdate(HospitalServiceRequest hospitalServiceRequest, @MappingTarget HospitalServiceEntity hospitalServiceEntity);
}
