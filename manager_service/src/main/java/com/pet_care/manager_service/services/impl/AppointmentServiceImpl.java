package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.entity.Appointment;
import com.pet_care.manager_service.repositories.AppointmentRepository;
import com.pet_care.manager_service.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointment(){
        return appointmentRepository.findAll();
    }

    public <S extends Appointment> S save(S entity) {
        return appointmentRepository.save(entity);
    }
}
