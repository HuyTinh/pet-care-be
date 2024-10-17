package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.entity.Appointment_Service;
import com.pet_care.manager_service.repositories.AppointmentServiceRepository;
import com.pet_care.manager_service.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceServiceImpl {
    @Autowired
    AppointmentServiceRepository appointmentServiceRepository;

    public Iterable<Appointment_Service> getAllAppointmentServices(){
        return appointmentServiceRepository.findAll();
    }

    public <S extends Appointment_Service> S save(S entity) {
        return appointmentServiceRepository.save(entity);
    }
}
