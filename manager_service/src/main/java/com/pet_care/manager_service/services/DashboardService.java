package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.AppointmentHomeDashboardTableResponse;
import com.pet_care.manager_service.dto.response.DashboardResponse;
import com.pet_care.manager_service.entity.Appointment;

public interface DashboardService {
    DashboardResponse getDashboardHome();

//    AppointmentHomeDashboardTableResponse appointmentHomeDashboardTable(Appointment appointment);
}
