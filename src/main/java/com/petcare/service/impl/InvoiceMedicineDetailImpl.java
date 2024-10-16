package com.petcare.service.impl;

import com.petcare.entity.InvoiceMedicineDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.InvoiceMedicineDetailRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceMedicineDetailImpl implements EntityService<InvoiceMedicineDetail, Long> {

    @Autowired
    private InvoiceMedicineDetailRepository invoiceMedicineDetailRepository;

    @Override
    public List<InvoiceMedicineDetail> getAllEntity() {

        List<InvoiceMedicineDetail> invoiceMedicineDetails = ArrayMapper
                .mapperIterableToList(invoiceMedicineDetailRepository.findAll());

        return invoiceMedicineDetails;
    }

    @Override
    public Optional<InvoiceMedicineDetail> getEntityById(Long id) {

        Optional<InvoiceMedicineDetail> invoiceMedicineDetail = invoiceMedicineDetailRepository.findById(id);

        return invoiceMedicineDetail;
    }

    @Override
    public InvoiceMedicineDetail createEntity(InvoiceMedicineDetail invoiceMedicineDetail) {

        Optional<InvoiceMedicineDetail> invoiceMedicineDetailOptional = invoiceMedicineDetailRepository.findById(invoiceMedicineDetail.getId());
        if (invoiceMedicineDetailOptional.isPresent()) {
            throw new APIException(ErrorCode.INVOICE_MEDICINE_DETAIL_ALREADY_EXISTS);
        }

        return invoiceMedicineDetailRepository.save(invoiceMedicineDetail);
    }

    @Override
    public InvoiceMedicineDetail updateEntity(InvoiceMedicineDetail invoiceMedicineDetail) {

        InvoiceMedicineDetail detail = invoiceMedicineDetailRepository.findById(invoiceMedicineDetail.getId())
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_MEDICINE_DETAIL_NOT_FOUND));

        return invoiceMedicineDetailRepository.save(invoiceMedicineDetail);
    }

    @Override
    public void deleteEntity(Long id) {

        InvoiceMedicineDetail invoiceMedicineDetail = invoiceMedicineDetailRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_MEDICINE_DETAIL_NOT_FOUND));

        invoiceMedicineDetailRepository.delete(invoiceMedicineDetail);
    }
}
