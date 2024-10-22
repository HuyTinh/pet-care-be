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
    public ResponseEntity<ApiResponse<List<CustomerPetAndServiceResponse>>> getAllCustomersTrue(){
        List<CustomerPetAndServiceResponse> listCustomer = customerService.getAllCustomersTrue();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Customers", listCustomer));
    }

    @GetMapping("/getAllCustomer")
    public ResponseEntity<ApiResponse<List<CustomerPetAndServiceResponse>>> getAllCustomers(){
        List<CustomerPetAndServiceResponse> listCustomer = customerService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Customers", listCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerPetAndServiceResponse>> deleteCustomer(@PathVariable Long id){
        CustomerPetAndServiceResponse customerResponse = customerService.deleteCustomer(id);
        return  ResponseEntity.ok(new ApiResponse<>(2000, "Get All Customers", null));
    }

}
