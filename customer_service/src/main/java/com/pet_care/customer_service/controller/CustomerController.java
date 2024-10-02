package com.pet_care.customer_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.response.ApiResponse;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {

    CustomerService customerService;

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        return ApiResponse.<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }


    @PostMapping("/create-appointment")
    public ApiResponse<CustomerResponse> createAppointment(@RequestBody AppointmentCreateRequest request, @RequestParam("emailNotification") boolean notification) throws JsonProcessingException {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.createAppointment(request, notification))
                .build();
    }

    @PutMapping("/account/{accountId}")
    public ApiResponse<CustomerResponse> updateCustomer(@PathVariable("accountId") Long accountId, @RequestBody CustomerCreateRequest
            customerRequest) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(accountId, customerRequest))
                .build();
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> getCustomerById(@PathVariable("customerId") Long customerId) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCustomerById(customerId))
                .build();
    }

    @GetMapping("/account/{accountId}")
    public ApiResponse<CustomerResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCustomerByAccountId(accountId))
                .build();
    }

    @DeleteMapping("/{customerId}")
    public ApiResponse<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.<Void>builder()
                .message("Customer deleted successfully")
                .build();
    }

}
