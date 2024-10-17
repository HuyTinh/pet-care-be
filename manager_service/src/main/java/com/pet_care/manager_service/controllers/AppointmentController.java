package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.entity.Appointment;
import com.pet_care.manager_service.services.impl.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/appointment")
@Tag(name = "Appointment - Controller")
public class AppointmentController {
    @Autowired
    AppointmentServiceImpl appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointment(){
        return ResponseEntity.ok(appointmentService.getAllAppointment());
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getById(@PathVariable("appointmentId") int appointmentId){
        return ResponseEntity.ok(new Appointment());
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment){
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    @PutMapping("{id}")
    public ResponseEntity<Appointment> update(@PathVariable int id,@RequestBody Appointment appointment){
        return ResponseEntity.ok(new Appointment());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Appointment> delete(@PathVariable int id){
        return ResponseEntity.ok(new Appointment());
    }
}
