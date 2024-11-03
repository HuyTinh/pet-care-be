package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.ServiceCRUDResponse;
import com.pet_care.manager_service.entity.Services;
import com.pet_care.manager_service.services.impl.ServicesServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("management/service")
@Tag(name = "Service - Controller")
public class ServiceController {

    @Autowired
    ServicesServiceImpl servicesService;

    @GetMapping
    public ResponseEntity<ApiResponse<Set<ServiceCRUDResponse>>> getAllService(){
        Set<ServiceCRUDResponse> listService = servicesService.getAllServices();
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get All Service Successful ", listService));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceCRUDResponse>> getServiceById(@PathVariable("serviceId") Long id){
        ServiceCRUDResponse service = servicesService.getServiceById(id);
        return ResponseEntity.ok(new ApiResponse<>(2000,"Get Service Successful ", service));
    }

    @PostMapping
    public ResponseEntity<Services> create(@RequestBody Services service){
        return ResponseEntity.ok(servicesService.save(service));
    }

    @PutMapping("{id}")
    public ResponseEntity<Services> update(@PathVariable("id") int id,@RequestBody Services service){
        return ResponseEntity.ok(new Services());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Services> delete(@PathVariable("id") int id){
        return ResponseEntity.ok(new Services());
    }
}
