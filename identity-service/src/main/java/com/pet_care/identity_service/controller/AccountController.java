package com.pet_care.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.identity_service.dto.request.AccountCreateRequest;
import com.pet_care.identity_service.dto.request.AccountUpdateRequest;
import com.pet_care.identity_service.dto.response.APIResponse;
import com.pet_care.identity_service.dto.response.AccountResponse;
import com.pet_care.identity_service.dto.response.AuthenticationResponse;
import com.pet_care.identity_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
     AccountService accountService;

    /**
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<List<AccountResponse>> getAllUser() {
        return APIResponse.<List<AccountResponse>>builder().code(1000).data(accountService.getAllUser()).build();
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    APIResponse<AccountResponse> getUserById(@PathVariable("id")  Long id) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.getUserById(id)).build();
    }

    /**
     * @param email
     * @return
     */
    @GetMapping("/email/{email}")
    APIResponse<AccountResponse> getUserByEmail(@PathVariable("email") String email) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.getUserByEmail(email)).build();
    }

    /**
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/generate-token")
    @ResponseStatus(HttpStatus.CREATED)
    APIResponse<AuthenticationResponse> createUserAndGenerateToken( @Valid @RequestBody AccountCreateRequest request) throws JsonProcessingException {
        return APIResponse.<AuthenticationResponse>builder().code(1000).data(accountService.createAccountAndCustomerRequest(request)).build();
    }

    /**
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    APIResponse<AccountResponse> createAccount(@RequestBody  AccountCreateRequest request) throws JsonProcessingException {
        log.info("{}", request);
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.createAccount(request)).build();
    }

    /**
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    APIResponse<AccountResponse> updateUser(@PathVariable("id")  Long id, @RequestBody @Valid AccountUpdateRequest request) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.updateAccount(id, request)).build();
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<String> deleteUser(@PathVariable("id")  Long id) {
        accountService.deleteAccount(id);
        return APIResponse.<String>builder().code(1000).message("Delete account successful").build();
    }

}
