package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.InvoiceReportResponse;

import java.time.LocalDate;

public interface InvoiceService {
    InvoiceReportResponse getInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date);
}
