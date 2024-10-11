package com.pet_care.appointment_service.service;

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
    @NotNull SpecieRepository specieRepository;

    @NotNull
    @Transactional(readOnly = true)
    public List<Specie> getAll() {
        return specieRepository.findAll();
    }
}
