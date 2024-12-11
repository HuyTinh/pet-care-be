package com.pet_care.employee_service.client;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@FeignClient(name = "identity-service")
@RequestMapping("/api/v1/identity-service")
public interface AccountClient {
    /**
     * @param request
     * @return
     */
    @PostMapping("/account")
    APIResponse<AccountResponse> createAccount(EmployeeCreateRequest request);
}
