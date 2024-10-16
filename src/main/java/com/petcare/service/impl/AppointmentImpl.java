package com.petcare.service.impl;

import com.petcare.entity.Appointment;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.AppointmentRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentImpl implements EntityService<Appointment, Long> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAllEntity() {
        Iterable<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> appointmentList = ArrayMapper.mapperIterableToList(appointments);
        return appointmentList;
    }

    @Override
    public Optional<Appointment> getEntityById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        System.out.println("id: " + id);
        return appointment;
    }

    @Override
    public Appointment createEntity(Appointment appointment) {
        Optional<Appointment> getAppointment = getEntityById(appointment.getId());
        if (getAppointment.isPresent()) {
            throw new APIException(ErrorCode.APPOINTMENT_ALREADY_EXISTS);
        }
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateEntity(Appointment appointment) {

        Appointment getAppointment = getEntityById(appointment.getId())
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteEntity(Long id) {

        Appointment getAppointment = getEntityById(id)
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        appointmentRepository.delete(getAppointment);

    }

}
