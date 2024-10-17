package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;
import com.pet_care.manager_service.entity.Account;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(CreateAccountRequest request);

    AccountResponse toAccountResponse(Account account);
}
