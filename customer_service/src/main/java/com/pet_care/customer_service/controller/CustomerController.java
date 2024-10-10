package com.pet_care.customer_service.controller;

import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.response.APIResponse;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
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
    public APIResponse<List<CustomerResponse>> getAllCustomer() {
        return APIResponse.<List<CustomerResponse>>builder()
                .data(customerService.getAllCustomer())
                .build();
    }


    @GetMapping("/{customerId}")
    public APIResponse<CustomerResponse> getCustomerById(@PathVariable("customerId") Long customerId) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.getCustomerById(customerId))
                .build();
    }


    @DeleteMapping("/{customerId}")
    public APIResponse<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return APIResponse.<Void>builder()
                .message("Customer deleted successfully")
                .build();
    }

    @GetMapping("/account/{accountId}")
    public APIResponse<CustomerResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.getCustomerByAccountId(accountId))
                .build();
    }

    @PutMapping(value = "/account/{accountId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<CustomerResponse> updateCustomer(
            @PathVariable("accountId") Long accountId,
            @ModelAttribute CustomerUpdateRequest customerRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.updateCustomer(accountId, customerRequest, files))
                .build();
    }
}
