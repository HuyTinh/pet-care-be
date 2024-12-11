package com.pet_care.appointment_service.mapper;

import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.entity.Appointment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {

    // Mapping from AppointmentCreateRequest to Appointment entity
    Appointment toEntity(AppointmentCreateRequest appointmentCreateRequest);

    // Mapping from Appointment entity to AppointmentResponse DTO
    AppointmentResponse toDto(Appointment appointment);

    // Partial update for Appointment using AppointmentUpdateRequest
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(AppointmentUpdateRequest appointmentUpdateRequest, @MappingTarget Appointment appointment);
}
