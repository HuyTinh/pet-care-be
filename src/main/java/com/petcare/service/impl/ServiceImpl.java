package com.petcare.service.impl;

import com.petcare.entity.Services;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.ServiceRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImpl implements EntityService<Services, Long> {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Services> getAllEntity() {
        return ArrayMapper.mapperIterableToList(serviceRepository.findAll());
    }

    @Override
    public Optional<Services> getEntityById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Services createEntity(Services service) {
        Optional<Services> serviceOptional = serviceRepository.findById(service.getId());
        if (serviceOptional.isPresent()) {
            throw new APIException(ErrorCode.SERVICE_ALREADY_EXISTS);
        }
        return serviceRepository.save(service);
    }

    @Override
    public Services updateEntity(Services service) {
        Services existingService = serviceRepository.findById(service.getId())
                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_NOT_FOUND));
        return serviceRepository.save(service);
    }

    @Override
    public void deleteEntity(Long id) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_NOT_FOUND));
        serviceRepository.delete(service);
    }
}

