package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest account);

    AccountResponse getAccountResponse(Long id);

    AccountResponse deleteAccount(Long id);

    List<AccountResponse> getAllEmployee();

    List<AccountResponse> getAllEmployeeTrue();

    List<AccountResponse> getAllByRole(Long id);
}
