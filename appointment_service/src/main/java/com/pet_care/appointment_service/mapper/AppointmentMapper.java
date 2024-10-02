package com.pet_care.appointment_service.mapper;

import com.pet_care.appointment_service.dto.request.AppointmentRequest;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.model.Appointment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    @Mapping(target = "services", ignore = true)
    Appointment toEntity(AppointmentRequest appointmentRequest);

    AppointmentResponse toDto(Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "services", ignore = true)
    Appointment partialUpdate(AppointmentRequest appointmentRequest, @MappingTarget Appointment appointment);
}