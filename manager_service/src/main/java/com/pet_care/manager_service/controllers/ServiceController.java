package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.entity.Services;
import com.pet_care.manager_service.services.impl.ServicesServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/service")
@Tag(name = "Service - Controller")
public class ServiceController {

    @Autowired
    ServicesServiceImpl servicesService;

    @GetMapping
    public ResponseEntity<List<Services>> getAllService(){
        return ResponseEntity.ok(servicesService.getAllService());
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<Services> getById(@PathVariable("serviceId") int serviceId){
        return ResponseEntity.ok(new Services());
    }

    @PostMapping
    public ResponseEntity<Services> create(@RequestBody Services service){
        return ResponseEntity.ok(servicesService.save(service));
    }

    @PutMapping("{id}")
    public ResponseEntity<Services> update(@PathVariable int id,@RequestBody Services service){
        return ResponseEntity.ok(new Services());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Services> delete(@PathVariable int id){
        return ResponseEntity.ok(new Services());
    }
}
