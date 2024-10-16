package com.petcare.service.impl;

import com.petcare.entity.ServiceType;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.ServiceTypeRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeImpl implements EntityService<ServiceType, Long> {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<ServiceType> getAllEntity() {
        return ArrayMapper.mapperIterableToList(serviceTypeRepository.findAll());
    }

    @Override
    public Optional<ServiceType> getEntityById(Long id) {
        return serviceTypeRepository.findById(id);
    }

    @Override
    public ServiceType createEntity(ServiceType serviceType) {
        Optional<ServiceType> serviceTypeOptional = serviceTypeRepository.findById(serviceType.getId());
        if (serviceTypeOptional.isPresent()) {
            throw new APIException(ErrorCode.SERVICE_TYPE_ALREADY_EXISTS);
        }
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public ServiceType updateEntity(ServiceType serviceType) {
        ServiceType existingServiceType = serviceTypeRepository.findById(serviceType.getId())
                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_TYPE_NOT_FOUND));
        return serviceTypeRepository.save(serviceType);
    }

    @Override
    public void deleteEntity(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.SERVICE_TYPE_NOT_FOUND));
        serviceTypeRepository.delete(serviceType);
    }
}

