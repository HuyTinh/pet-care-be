package com.pet_care.warehouse_manager_service.service.serviceImpl;

import com.pet_care.warehouse_manager_service.entity.Manufacturer;
import com.pet_care.warehouse_manager_service.repositories.ManufacturerRepository;
import com.pet_care.warehouse_manager_service.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    ManufacturerRepository manufacturerRepository;

    public List<Manufacturer> getAllManufacturer() {
        return manufacturerRepository.findAll();
    }

    public <S extends Manufacturer> S create(S entity) {
        return manufacturerRepository.save(entity);
    }

    public <S extends Manufacturer> S update(S entity) {
        return manufacturerRepository.save(entity);
    }
}
