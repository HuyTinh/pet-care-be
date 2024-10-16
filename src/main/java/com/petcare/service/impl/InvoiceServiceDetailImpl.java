package com.petcare.service.impl;

import com.petcare.entity.InvoiceServiceDetail;
import com.petcare.entity.InvoiceServiceDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.InvoiceServiceDetailRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceDetailImpl implements EntityService<InvoiceServiceDetail, Long> {

    @Autowired
    private InvoiceServiceDetailRepository invoiceServiceDetailRepository;

    @Override
    public List<InvoiceServiceDetail> getAllEntity() {

        List<InvoiceServiceDetail> InvoiceServiceDetails = ArrayMapper
                .mapperIterableToList(invoiceServiceDetailRepository.findAll());

        return InvoiceServiceDetails;
    }

    @Override
    public Optional<InvoiceServiceDetail> getEntityById(Long id) {

        Optional<InvoiceServiceDetail> invoiceServiceDetail = invoiceServiceDetailRepository.findById(id);

        return invoiceServiceDetail;
    }

    @Override
    public InvoiceServiceDetail createEntity(InvoiceServiceDetail invoiceServiceDetail) {

        Optional<InvoiceServiceDetail> invoiceServiceDetailOptional = invoiceServiceDetailRepository.findById(invoiceServiceDetail.getId());
        if (invoiceServiceDetailOptional.isPresent()) {
            throw new APIException(ErrorCode.INVOICE_SERVICE_DETAIL_ALREADY_EXISTS);
        }

        return invoiceServiceDetailRepository.save(invoiceServiceDetail);
    }

    @Override
    public InvoiceServiceDetail updateEntity(InvoiceServiceDetail invoiceServiceDetail) {

        InvoiceServiceDetail detail = invoiceServiceDetailRepository.findById(invoiceServiceDetail.getId())
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_SERVICE_DETAIL_NOT_FOUND));

        return invoiceServiceDetailRepository.save(invoiceServiceDetail);
    }

    @Override
    public void deleteEntity(Long id) {

        InvoiceServiceDetail invoiceServiceDetail = invoiceServiceDetailRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_SERVICE_DETAIL_NOT_FOUND));

        invoiceServiceDetailRepository.delete(invoiceServiceDetail);
    }
}