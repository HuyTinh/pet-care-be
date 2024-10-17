package com.pet_care.manager_service.controllers;


import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.CustomerPetAndServiceResponse;
import com.pet_care.manager_service.entity.Customer;
import com.pet_care.manager_service.services.impl.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/customer")
@Tag(name = "Customer - Controller")
public class CustomerController {

    @Autowired
    CustomerServiceImpl customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerPetAndServiceResponse>>> getAllOwner(){
        List<CustomerPetAndServiceResponse> listCustomer = customerService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Customers", listCustomer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer){
        return ResponseEntity.ok(customerService.save(customer));
    }

}
