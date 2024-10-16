package com.petcare.service.impl;

import com.petcare.entity.AppointmentService;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.AppointmentServiceRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements EntityService<AppointmentService, Long> {

    @Autowired
    private AppointmentServiceRepository appointmentServiceRepository;

    @Override
    public List<AppointmentService> getAllEntity() {

        List<AppointmentService> appointmentServices =
                ArrayMapper.mapperIterableToList(appointmentServiceRepository.findAll());

        return appointmentServices;
    }

    @Override
    public Optional<AppointmentService> getEntityById(Long id) {

        Optional<AppointmentService> appointmentServiceOptional = appointmentServiceRepository.findById(id);

        return appointmentServiceOptional;
    }

    @Override
    public AppointmentService createEntity(AppointmentService appointmentService) {

        Optional<AppointmentService> appointmentServiceOptional = appointmentServiceRepository.findById(appointmentService.getId());
        if (appointmentServiceOptional.isPresent()) {
            throw new APIException(ErrorCode.APPOINTMENT_SERVICE_ALREADY_EXISTS);
        }

        return appointmentServiceRepository.save(appointmentService);
    }

    @Override
    public AppointmentService updateEntity(AppointmentService appointmentService) {

        AppointmentService appointmentServiceOptional =
                appointmentServiceRepository.findById(appointmentService.getId())
                        .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_SERVICE_NOT_FOUND));

        return appointmentServiceRepository.save(appointmentService);
    }

    @Override
    public void deleteEntity(Long id) {

        AppointmentService appointmentServiceOptional =
                appointmentServiceRepository.findById(id)
                        .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_SERVICE_NOT_FOUND));

        appointmentServiceRepository.delete(appointmentServiceOptional);
    }
}
