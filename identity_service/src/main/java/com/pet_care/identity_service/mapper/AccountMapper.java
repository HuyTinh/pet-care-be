package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.AccountCreateRequest;
import com.pet_care.identity_service.dto.request.AccountUpdateRequest;
import com.pet_care.identity_service.dto.request.CustomerCreateRequest;
import com.pet_care.identity_service.dto.response.AccountResponse;
import com.pet_care.identity_service.model.Account;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    /**
     * @param accountCreateRequest
     * @return
     */
    @Mapping(target = "roles", ignore = true)
    Account toEntity(AccountCreateRequest accountCreateRequest);

    /**
     * @param account
     * @return
     */
    AccountResponse toDto(Account account);

    /**
     * @param account
     * @return
     */
    CustomerCreateRequest toCustomerRequest(AccountCreateRequest account);

    /**
     * @param userCreationRequest
     * @param account
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    Account partialUpdate(AccountUpdateRequest userCreationRequest, @MappingTarget Account account);
}