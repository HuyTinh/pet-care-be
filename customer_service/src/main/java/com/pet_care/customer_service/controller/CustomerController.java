package com.pet_care.customer_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.response.APIResponse;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {

    CustomerService customerService;

    @GetMapping
    public APIResponse<List<CustomerResponse>> getAllCustomers() {
        return APIResponse.<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }

    @PostMapping("/create-appointment")
    public APIResponse<CustomerResponse> createAppointment(@RequestBody AppointmentCreateRequest request, @RequestParam("emailNotification") boolean notification) throws JsonProcessingException {
        return APIResponse.<CustomerResponse>builder()
                .result(customerService.createAppointment(request, notification))
                .build();
    }

    @PutMapping("/account/{accountId}")
    public APIResponse<CustomerResponse> updateCustomer(
            @PathVariable("accountId") Long accountId,
            @RequestBody CustomerCreateRequest customerRequest,
            @RequestPart("files") List<MultipartFile> files
    ) {
        return APIResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(accountId, customerRequest, files))
                .build();
    }

    @GetMapping("/{customerId}")
    public APIResponse<CustomerResponse> getCustomerById(@PathVariable("customerId") Long customerId) {
        return APIResponse.<CustomerResponse>builder()
                .result(customerService.getCustomerById(customerId))
                .build();
    }

    @GetMapping("/account/{accountId}")
    public APIResponse<CustomerResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        return APIResponse.<CustomerResponse>builder()
                .result(customerService.getCustomerByAccountId(accountId))
                .build();
    }

    @DeleteMapping("/{customerId}")
    public APIResponse<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return APIResponse.<Void>builder()
                .message("Customer deleted successfully")
                .build();
    }

}
