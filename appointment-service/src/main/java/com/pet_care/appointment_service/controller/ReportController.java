package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.ReportAppointmentByDateToDateResponse;
import com.pet_care.appointment_service.dto.response.ReportAppointmentByYearResponse;
import com.pet_care.appointment_service.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/{year}")
    public APIResponse<List<ReportAppointmentByYearResponse>> getReportAppointmentByYear(
            @PathVariable("year") Integer year
    ) {
        return APIResponse.<List<ReportAppointmentByYearResponse>>builder()
                .data(reportService.getAppointmentReportByYear(year))
                .build();
    }
    @GetMapping("/date-to-date")
    public APIResponse<List<ReportAppointmentByDateToDateResponse>> getReportAppointmentByDateToDate(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        return APIResponse.<List<ReportAppointmentByDateToDateResponse>>builder()
                .data(reportService.getReportAppointmentByDateToDate(startDate, endDate))
                .build();
    }
}
