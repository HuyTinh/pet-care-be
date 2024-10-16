package com.petcare.service.impl;

import com.petcare.entity.PrescriptionDetail;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.PrescriptionDetailRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionDetailImpl implements EntityService<PrescriptionDetail, Long> {

    @Autowired
    private PrescriptionDetailRepository prescriptionDetailRepository;

    @Override
    public List<PrescriptionDetail> getAllEntity() {
        return ArrayMapper.mapperIterableToList(prescriptionDetailRepository.findAll());
    }

    @Override
    public Optional<PrescriptionDetail> getEntityById(Long id) {
        return prescriptionDetailRepository.findById(id);
    }

    @Override
    public PrescriptionDetail createEntity(PrescriptionDetail prescriptionDetail) {
        Optional<PrescriptionDetail> prescriptionDetailOptional = prescriptionDetailRepository.findById(prescriptionDetail.getId());
        if (prescriptionDetailOptional.isPresent()) {
            throw new APIException(ErrorCode.PRESCRIPTION_DETAIL_ALREADY_EXISTS);
        }
        return prescriptionDetailRepository.save(prescriptionDetail);
    }

    @Override
    public PrescriptionDetail updateEntity(PrescriptionDetail prescriptionDetail) {
        PrescriptionDetail existingDetail = prescriptionDetailRepository.findById(prescriptionDetail.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_DETAIL_NOT_FOUND));
        return prescriptionDetailRepository.save(prescriptionDetail);
    }

    @Override
    public void deleteEntity(Long id) {
        PrescriptionDetail prescriptionDetail = prescriptionDetailRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_DETAIL_NOT_FOUND));
        prescriptionDetailRepository.delete(prescriptionDetail);
    }
}

