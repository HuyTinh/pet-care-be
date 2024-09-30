package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.model.Specie;
import com.pet_care.appointment_service.repository.SpecieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecieService {
    SpecieRepository specieRepository;

    public List<Specie> getAll() {
        return specieRepository.findAll();
    }
}
