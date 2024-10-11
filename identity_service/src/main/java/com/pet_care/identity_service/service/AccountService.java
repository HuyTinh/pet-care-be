package com.pet_care.identity_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.identity_service.dto.request.AccountCreateRequest;
import com.pet_care.identity_service.dto.request.AccountUpdateRequest;
import com.pet_care.identity_service.dto.request.AuthenticationRequest;
import com.pet_care.identity_service.dto.request.CustomerCreateRequest;
import com.pet_care.identity_service.dto.response.AccountResponse;
import com.pet_care.identity_service.dto.response.AuthenticationResponse;
import com.pet_care.identity_service.exception.APIException;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.mapper.AccountMapper;
import com.pet_care.identity_service.model.Account;
import com.pet_care.identity_service.repository.AccountRepository;
import com.pet_care.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {

    @NotNull AccountRepository accountRepository;

    @NotNull AccountMapper accountMapper;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @NotNull RoleRepository roleRepository;

    @NotNull MessageService messageService;

    @NotNull AuthenticationService authenticationService;

    @NotNull ObjectMapper objectMapper;

    @NotNull
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllUser() {
        return accountRepository.findAll().stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AuthenticationResponse createAccountAndCustomerRequest(@NotNull AccountCreateRequest request) throws JsonProcessingException {
        if (accountRepository.existsByEmail(request.getEmail()))
            throw new APIException(ErrorCode.USER_EXISTED);

        Account account = accountMapper.toEntity(request);

        account.setPassword(passwordEncoder.encode(request.getPassword()));

        account.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));

        Account saveAccount = accountRepository.save(account);

        CustomerCreateRequest customerCreateRequest = accountMapper.toCustomerRequest(request);
        customerCreateRequest.setAccountId(saveAccount.getId());

        messageService.sendMessageQueue("customer-create-queue", objectMapper.writeValueAsString(customerCreateRequest));

        return authenticationService.authenticate(AuthenticationRequest.builder().email(saveAccount.getEmail()).password(request.getPassword()).build());
    }

    @Transactional
    public AccountResponse createAccountRequest(@NotNull AccountCreateRequest request) {

        Account account = accountMapper.toEntity(request);

        account.setPassword(passwordEncoder.encode(request.getPassword()));

        account.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));

        Account saveAccount = accountRepository.save(account);

        return accountMapper.toDto(saveAccount);
    }

    @Transactional
    public AccountResponse updateRequest(@NotNull Long id, AccountUpdateRequest request) {
        Account existAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        return accountMapper.toDto(accountRepository.save(accountMapper.partialUpdate(request, existAccount)));
    }

    @Transactional
    public void deleteRequest(@NotNull Long id) {
        accountRepository.deleteById(id);
    }

    @PostAuthorize("returnObject.email == authentication.name || hasRole('HOSPITAL_ADMINISTRATOR')")
    @Transactional(readOnly = true)
    public AccountResponse getUserById(@NotNull Long id) {
        return accountMapper
                .toDto(accountRepository
                        .findById(id)
                        .orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_EXISTED)));
    }

    @Transactional(readOnly = true)
    public AccountResponse getUserByEmail(String email) {
        return accountMapper
                .toDto(accountRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_EXISTED)));
    }

}
