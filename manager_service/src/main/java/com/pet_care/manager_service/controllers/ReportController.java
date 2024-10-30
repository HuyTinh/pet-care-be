package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.InvoiceReportResponse;
import com.pet_care.manager_service.dto.response.InvoiceResponse;
import com.pet_care.manager_service.dto.response.RevenueAndAppointmentResponse;
import com.pet_care.manager_service.entity.Invoice;
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
}
