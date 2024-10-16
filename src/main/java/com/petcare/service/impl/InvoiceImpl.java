package com.petcare.service.impl;

import com.petcare.entity.Invoice;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.InvoiceRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceImpl implements EntityService<Invoice, Long> {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> getAllEntity() {

        List<Invoice> invoices = ArrayMapper.mapperIterableToList(invoiceRepository.findAll());

        return invoices;
    }

    @Override
    public Optional<Invoice> getEntityById(Long id) {

        Optional<Invoice> invoice = invoiceRepository.findById(id);

        return invoice;
    }

    @Override
    public Invoice createEntity(Invoice invoice) {

        Invoice getInvoice = getEntityById(invoice.getId())
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_ALREADY_EXISTS));

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateEntity(Invoice invoice) {

        Invoice getInvoice = getEntityById(invoice.getId())
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));

        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteEntity(Long id) {

        Invoice invoice = getEntityById(id)
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));

        invoiceRepository.delete(invoice);
    }
}
