package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.InvoiceReportResponse;
import com.pet_care.manager_service.services.impl.InvoiceServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/management/report")
@Tag(name = "Dashboard - Controller")
public class ReportController {
    @Autowired
    InvoiceServiceImpl invoiceService;

    @GetMapping()
    public ResponseEntity<ApiResponse<InvoiceReportResponse>> getInvoiceReport(
            @RequestParam(required = false) LocalDate create_date,
            @RequestParam(required = false) LocalDate from_date,
            @RequestParam(required = false) LocalDate to_date
    ) {
        if(create_date ==  null){
            create_date = LocalDate.now();
        }
        InvoiceReportResponse invoiceReportResponse = invoiceService.getInvoiceReport(from_date,to_date,create_date);
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get Revenue", invoiceReportResponse));
    }
}
