package com.petcare.service.impl;

import com.petcare.entity.Species;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.SpeciesRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpeciesImpl implements EntityService<Species, Long> {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Override
    public List<Species> getAllEntity() {
        return ArrayMapper.mapperIterableToList(speciesRepository.findAll());
    }

    @Override
    public Optional<Species> getEntityById(Long id) {
        return speciesRepository.findById(id);
    }

    @Override
    public Species createEntity(Species species) {
        Optional<Species> speciesOptional = speciesRepository.findById(species.getId());
        if (speciesOptional.isPresent()) {
            throw new APIException(ErrorCode.SPECIES_ALREADY_EXISTS);
        }
        return speciesRepository.save(species);
    }

    @Override
    public Species updateEntity(Species species) {
        Species existingSpecies = speciesRepository.findById(species.getId())
                .orElseThrow(() -> new APIException(ErrorCode.SPECIES_NOT_FOUND));
        return speciesRepository.save(species);
    }

    @Override
    public void deleteEntity(Long id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.SPECIES_NOT_FOUND));
        speciesRepository.delete(species);
    }
}

