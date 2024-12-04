package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.dto.response.VeterinaryCareResponse;
import com.pet_care.medical_prescription_service.entity.VeterinaryCare;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.VeterinaryCareMapper;
import com.pet_care.medical_prescription_service.repository.VeterinaryCareRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VeterinaryCareService {
    VeterinaryCareMapper veterinaryCareMapper;

    VeterinaryCareRepository veterinaryCareRepository;


    @Transactional(readOnly = true)
    public List<VeterinaryCareResponse> getAllVeterinaryCare() {
        return veterinaryCareRepository.findAll().stream().map(veterinaryCareMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VeterinaryCareResponse getVeterinaryCareById(String name) {
        return veterinaryCareMapper.toDto(veterinaryCareRepository.findById(name).orElseThrow(() -> new APIException(ErrorCode.VETERINARY_CARE_NOT_FOUND)));
    }

}
