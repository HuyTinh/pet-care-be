package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.entity.Services;
import com.pet_care.manager_service.repositories.ServicesRepository;
import com.pet_care.manager_service.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    ServicesRepository servicesRepository;

    public List<Services> getAllService(){
        return servicesRepository.findAll();
    }

    public <S extends Services> S save(S entity) {
        return servicesRepository.save(entity);
    }
}
