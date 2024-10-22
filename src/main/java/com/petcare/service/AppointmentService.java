package com.petcare.service;

import com.petcare.entity.Appointment;
import com.petcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService
{

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointment(long petId)
    {
        System.out.println("0");
        List<Appointment> appointments = appointmentRepository.findAppointmentByPetId(petId);
        System.out.println("1");

        return appointments;
    }

}
