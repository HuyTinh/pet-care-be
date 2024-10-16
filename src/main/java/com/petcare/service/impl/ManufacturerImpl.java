package com.petcare.service.impl;

import com.petcare.entity.Manufacturer;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.ManufacturerRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerImpl implements EntityService<Manufacturer, Long> {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public List<Manufacturer> getAllEntity() {

        List<Manufacturer> manufacturers = ArrayMapper.mapperIterableToList(manufacturerRepository.findAll());

        return manufacturers;
    }

    @Override
    public Optional<Manufacturer> getEntityById(Long id) {

        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);

        return manufacturer;
    }

    @Override
    public Manufacturer createEntity(Manufacturer manufacturer) {

        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(manufacturer.getId());
        if (manufacturerOptional.isPresent()) {
            throw new APIException(ErrorCode.MANUFACTURER_ALREADY_EXISTS);
        }

        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer updateEntity(Manufacturer manufacturer) {

        Manufacturer manufacturerOptional = manufacturerRepository.findById(manufacturer.getId())
                .orElseThrow(() -> new APIException(ErrorCode.MANUFACTURER_NOT_FOUND));

        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public void deleteEntity(Long id) {

        Manufacturer manufacturerOptional = manufacturerRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MANUFACTURER_NOT_FOUND));

        manufacturerRepository.delete(manufacturerOptional);
    }
}
