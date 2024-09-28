package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.AccountCreationRequest;
import com.pet_care.identity_service.dto.request.AccountUpdateRequest;
import com.pet_care.identity_service.dto.request.sub.CustomerCreationRequest;
import com.pet_care.identity_service.dto.response.AccountResponse;
import com.pet_care.identity_service.model.Account;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    @Mapping(target = "roles", ignore = true)
    Account toEntity(AccountCreationRequest accountCreationRequest);

    AccountResponse toDto(Account account);

    CustomerCreationRequest toCustomerRequest(AccountCreationRequest account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    Account partialUpdate(AccountUpdateRequest userCreationRequest, @MappingTarget Account account);
}