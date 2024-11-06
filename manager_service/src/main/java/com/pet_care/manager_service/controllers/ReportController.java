package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.Invoice;
import com.pet_care.manager_service.services.impl.DashboardServiceImpl;
import com.pet_care.manager_service.services.impl.InvoiceServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/management/report")
@Tag(name = "Dashboard - Controller")
public class ReportController {
    @Autowired
    InvoiceServiceImpl invoiceService;
    @Autowired
    DashboardServiceImpl dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<InvoiceReportResponse>> getInvoiceReport(
            @RequestParam(required = false) LocalDate from_date,
            @RequestParam(required = false) LocalDate to_date,
            @RequestParam(required = false) LocalDate create_date
    ) {
        if (create_date == null && from_date == null && to_date == null) {
            create_date = LocalDate.now();
        }
        InvoiceReportResponse invoiceReportResponse = invoiceService.getInvoiceReport(from_date,to_date,create_date);
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get Revenue", invoiceReportResponse));
    }

    @GetMapping("/revenueByYear")
    public ResponseEntity<ApiResponse<Set<RevenueAndAppointmentResponse>>> getRevenueAndAppointmentByYear(
            @RequestParam(required = false) Long years
    ){
        Set<RevenueAndAppointmentResponse> revenueAndAppointment = invoiceService.getRevenueAndAppointment(years);
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get Revenue And Appointment ", revenueAndAppointment));
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable("id") Long id){
        InvoiceResponse invoiceResponse = invoiceService.getInvoice(id);
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get Invoice ", invoiceResponse));
    }

    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable("id") Long id){
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Delete Invoice Succes" , null));
    }

    @GetMapping("/appointment")
    public ResponseEntity<ApiResponse<AppointmentFromAndTodateResponse>> getAppointmentFromAndToDate(
            @RequestParam(required = false) LocalDate from_date,
            @RequestParam(required = false) LocalDate to_date,
            @RequestParam(required = false) Long month,
            @RequestParam(required = false) Long year,
            @RequestParam(required = false) Long year_first,
            @RequestParam(required = false) Long year_second
    ){

        AppointmentFromAndTodateResponse appointment = dashboardService.appFromAndTodateResponse(from_date,to_date,month,year,year_first,year_second);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Appointment Chart Success", appointment));
    }


    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<InvoiceCountResponse>> getRevenue(
            @RequestParam(required = false) Long month,
            @RequestParam(required = false) Long year,
            @RequestParam(required = false) Boolean today,
            @RequestParam(required = false) Long year_first,
            @RequestParam(required = false) Long year_second
    ){

        InvoiceCountResponse invoiceCountResponse = dashboardService.invoiceCountResponse(month,year,today,year_first,year_second);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Revenue Chart Success", invoiceCountResponse));
    }
}
