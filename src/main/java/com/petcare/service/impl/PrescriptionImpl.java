package com.petcare.service.impl;

import com.petcare.entity.Prescription;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.PrescriptionRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionImpl implements EntityService<Prescription, Long> {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Override
    public List<Prescription> getAllEntity() {
        return ArrayMapper.mapperIterableToList(prescriptionRepository.findAll());
    }

    @Override
    public Optional<Prescription> getEntityById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public Prescription createEntity(Prescription prescription) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(prescription.getId());
        if (prescriptionOptional.isPresent()) {
            throw new APIException(ErrorCode.PRESCRIPTION_ALREADY_EXISTS);
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Prescription updateEntity(Prescription prescription) {
        Prescription existingPrescription = prescriptionRepository.findById(prescription.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deleteEntity(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        prescriptionRepository.delete(prescription);
    }
}

