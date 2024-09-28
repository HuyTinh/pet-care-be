package com.pet_care.appointment_service.repository.httpClient;

import com.pet_care.appointment_service.dto.response.ApiResponse;
import com.pet_care.appointment_service.dto.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:8084/api/v1")
public interface CustomerClient {

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CustomerResponse> getCustomer(@PathVariable("customerId") String customerId);

    @GetMapping(value = "/customer/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CustomerResponse> getCustomerByAccountId(@PathVariable("accountId") String accountId);
}
