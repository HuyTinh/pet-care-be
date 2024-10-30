package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.InvoiceReportResponse;
import com.pet_care.manager_service.dto.response.InvoiceResponse;
import com.pet_care.manager_service.dto.response.RevenueAndAppointmentResponse;

import java.time.LocalDate;
import java.util.Set;

public interface InvoiceService {
    InvoiceReportResponse getInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date);

    Set<InvoiceResponse> getSetInvoice(LocalDate from_date, LocalDate to_date, LocalDate create_date);

    InvoiceResponse getInvoice(Long id);

    Set<RevenueAndAppointmentResponse> getRevenueAndAppointment(Long id);

    InvoiceResponse deleteInvoice(Long id);

}
