package com.pet_care.bill_service.service;

import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.exception.APIException;
import com.pet_care.bill_service.exception.ErrorCode;
import com.pet_care.bill_service.mapper.InvoiceMapper;
import com.pet_care.bill_service.repository.InvoiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;

    InvoiceMapper invoiceMapper;

    /**
     * @return
     */
    public List<InvoiceResponse> getAllInvoice() {

        List<InvoiceResponse> invoiceResponseList = invoiceRepository.findAll().stream().map(invoiceMapper::toDto).toList();

        log.info("Get all invoice");

        return invoiceResponseList;
    }

    /**
     * @param id
     * @return
     */
    public InvoiceResponse getInvoiceById(Long id) {
        InvoiceResponse invoiceResponse = invoiceRepository.findById(id).map(invoiceMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));

        log.info("Get invoice by id: {}", id);

        return invoiceResponse;
    }
}
