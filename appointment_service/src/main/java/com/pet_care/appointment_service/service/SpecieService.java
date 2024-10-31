package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.model.Specie;
import com.pet_care.appointment_service.repository.SpecieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecieService {
     SpecieRepository specieRepository;

    /**
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<Specie> getAll() {
        return specieRepository.findAll();
    }

    /**
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public Specie getByName( String name) {
        return specieRepository.findById(name).orElseThrow(() -> new APIException(ErrorCode.SPECIE_NOT_FOUND));
    }
}
