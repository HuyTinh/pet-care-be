package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.AccountCreateRequest;
import com.pet_care.identity_service.dto.request.AccountUpdateRequest;
import com.pet_care.identity_service.dto.request.CustomerCreateRequest;
import com.pet_care.identity_service.dto.response.AccountResponse;
import com.pet_care.identity_service.model.Account;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    @Mapping(target = "roles", ignore = true)
    Account toEntity(AccountCreateRequest accountCreateRequest);

    AccountResponse toDto(Account account);

    CustomerCreateRequest toCustomerRequest(AccountCreateRequest account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    Account partialUpdate(AccountUpdateRequest userCreationRequest, @MappingTarget Account account);
}