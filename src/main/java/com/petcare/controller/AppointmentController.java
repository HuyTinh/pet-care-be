package com.petcare.controller;

import com.petcare.entity.Appointment;
import com.petcare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController
{

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/{id}")
    public List<Appointment> getAppointments(@PathVariable long id)
    {
        System.out.println("start");

        List<Appointment> appointments = appointmentService.getAllAppointment(id);

        System.out.println("end");
        return appointments;
    }

}
