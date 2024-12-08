package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.dto.response.ReportAppointmentByYearResponse;
import com.pet_care.appointment_service.repository.AppointmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReportService {
    AppointmentRepository appointmentRepository;

    public List<ReportAppointmentByYearResponse> getAppointmentReportByYear(int year) {
        return appointmentRepository.getAppointmentsReportByYear(year);
    }
}
