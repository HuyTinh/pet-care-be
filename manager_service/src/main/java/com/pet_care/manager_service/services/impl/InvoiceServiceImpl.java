package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.InvoiceReportResponse;
import com.pet_care.manager_service.entity.Invoice;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.repositories.InvoiceRepository;
import com.pet_care.manager_service.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public InvoiceReportResponse getInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date) {
        return convertInvoiceReport(from_date, to_date, create_date);
    }

    public InvoiceReportResponse convertInvoiceReport(LocalDate from_date, LocalDate to_date, LocalDate create_date) {
        Optional<Object[]> getRevenue = invoiceRepository.getRevenueByDate(from_date, to_date, create_date);
        if(getRevenue.isEmpty()){
            throw new AppException(ErrorCode.INVOICE_NOT_EXIST);
        }
        Object[] invoiceReport = (Object[]) getRevenue.get()[0];
        Long count_invoice = (Long) invoiceReport[0];
        Double total_price = (Double) invoiceReport[1];

        return InvoiceReportResponse.builder()
                .count_invoice(count_invoice)
                .total_invoice(total_price)
                .build();
    }

}
