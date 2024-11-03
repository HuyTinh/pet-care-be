package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;
import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.services.impl.AccountServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/account")
@Tag(name = "Account - Controller")
public class AccountController {

    @Autowired
    AccountServiceImpl accountService;

//    @GetMapping
//    public ResponseEntity<List<Account>> getAllAccount(){
////        System.out.println(accountService.getAllEmployee());
//        return ResponseEntity.ok(accountService.getAllAccount());
//    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getById(@PathVariable("accountId") Long accountId){
        AccountResponse accountResponse = accountService.getAccountResponse(accountId);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Successful",accountResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(@RequestBody CreateAccountRequest account){
        AccountResponse addAccount = accountService.createAccount(account);
        return ResponseEntity.status(201).body(new ApiResponse<>(2000, "Create Employee Successful",addAccount));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> delete(@PathVariable Long id){
        AccountResponse deleteAccount = accountService.deleteAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Delete Employee Successful",null));
    }

    @GetMapping("/employee/all")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllEmployee(){
        List<AccountResponse> allEmployee = accountService.getAllEmployee();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Employee Successful",allEmployee));
    }

    @GetMapping("/employee/role/{id}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllEmployeeRole(@PathVariable Long id){
        List<AccountResponse> listEmployeeByRole = accountService.getAllByRole(id);
        AccountResponse accountResponse = listEmployeeByRole.get(0);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All "+ accountResponse.getProfile().getRole().getName() +" Successful",listEmployeeByRole));
    }

    @GetMapping("/employee")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllEmployeeTrue(){
        List<AccountResponse> allEmployeeTrue = accountService.getAllEmployeeTrue();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Employee Successful",allEmployeeTrue));
    }
}
