package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.request.CreateAccountRequest;
import com.pet_care.manager_service.dto.response.AccountResponse;
import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.PageableResponse;
import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.enums.RoleEnum;
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

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getById(@PathVariable("employeeId") Long accountId){
        AccountResponse accountResponse = accountService.getAccountResponse(accountId);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Successful",accountResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(@RequestBody CreateAccountRequest account){
        AccountResponse addAccount = accountService.createAccount(account);
        return ResponseEntity.status(201).body(new ApiResponse<>(2000, "Create Employee Successful",addAccount));
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> delete(@PathVariable Long id){
        AccountResponse deleteAccount = accountService.deleteAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Delete Employee Successful",null));
    }
    /**
     * getAllEmployee
     * @return
     */
    @GetMapping("/employee/all")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllEmployee(){
        List<AccountResponse> allEmployee = accountService.getAllEmployee();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Employee Successful",allEmployee));
    }
    /**
     * @param role_name
     * @return
     */
    @GetMapping("/employee/role/{role_name}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllEmployeeRole(
            @RequestParam(required = false) RoleEnum role_name
    ){
        List<AccountResponse> listEmployeeByRole = accountService.getAllByRole(role_name );
        AccountResponse accountResponse = listEmployeeByRole.get(0);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All "+ accountResponse.getProfile().getRole().getName() +" Successful",listEmployeeByRole));
    }
    /**
     * getAllEmployeeTrue : với status = true
     * @return
     */
    @GetMapping("/employee")
    public ResponseEntity<ApiResponse<PageableResponse<AccountResponse>>> getAllEmployeeTrue(
            @RequestParam(required = false) String search_query,
            @RequestParam(defaultValue = "0") int page_number,
            @RequestParam(defaultValue = "50") int page_size
    ){
        PageableResponse<AccountResponse> allEmployeeTrue = accountService.getAllEmployeeTrue(search_query,page_number,page_size);
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Employee Successful",allEmployeeTrue));
    }
}
