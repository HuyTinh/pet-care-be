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

    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<List<AccountResponse>> getAllUser() {
        return APIResponse.<List<AccountResponse>>builder().code(1000).data(accountService.getAllUser()).build();
    }

    @GetMapping("/{id}")
    APIResponse<AccountResponse> getUserById(@PathVariable("id") Long id) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.getUserById(id)).build();
    }

    @GetMapping("/email/{email}")
    APIResponse<AccountResponse> getUserByEmail(@PathVariable("email") String email) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.getUserByEmail(email)).build();
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    APIResponse<AccountResponse> createUser(@Valid @RequestBody AccountCreateRequest request) {
//        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.createRequest(request)).build();
//    }

    @PostMapping("/generate-token")
    @ResponseStatus(HttpStatus.CREATED)
    APIResponse<AuthenticationResponse> createUserAndGenerateToken(@Valid @RequestBody AccountCreateRequest request) throws JsonProcessingException {
        return APIResponse.<AuthenticationResponse>builder().code(1000).data(accountService.createRequest(request)).build();
    }

    @PutMapping("/{id}")
    APIResponse<AccountResponse> updateUser(@PathVariable("id") Long id, @RequestBody @Valid AccountUpdateRequest request) {
        return APIResponse.<AccountResponse>builder().code(1000).data(accountService.updateRequest(id, request)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<String> deleteUser(@PathVariable("id") Long id) {
        accountService.deleteRequest(id);
        return APIResponse.<String>builder().code(1000).message("Delete account successful").build();
    }

}
