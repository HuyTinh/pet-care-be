package com.pet_care.employee_service.client;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(name = "IDENTITY-SERVICE")
public interface AccountClient {
    /**
     * @param request
     * @return
     */
    @PostMapping("/api/v1/identity-service/account")
    APIResponse<AccountResponse> createAccount(EmployeeCreateRequest request);
}
