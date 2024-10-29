package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.AppointmentHomeDashboardTableResponse;
import com.pet_care.manager_service.entity.Appointment;

import java.util.Set;

public interface AppointmentService {
    Set<AppointmentHomeDashboardTableResponse> searchAppointmentYesterday();

    AppointmentHomeDashboardTableResponse deleteAppointment(Long id);

    AppointmentHomeDashboardTableResponse getAppointmentById(Long id);
}
