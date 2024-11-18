package com.pet_care.customer_service.controller;

// Import necessary classes for handling requests and responses
import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.response.APIResponse;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Mark this class as a REST controller, handling requests at the "/customer" endpoint
@RestController
@RequestMapping("customer")
@RequiredArgsConstructor // Automatically generates a constructor for required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Set fields to private and final
public class CustomerController {

    // Inject the CustomerService dependency
    CustomerService customerService;

    /**
     * Retrieves a list of all customers.
     *
     * @return APIResponse containing a list of CustomerResponse objects
     */
    @GetMapping
    public APIResponse<List<CustomerResponse>> getAllCustomer() {
        return APIResponse.<List<CustomerResponse>>builder()
                .data(customerService.getAllCustomer())
                .build();
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId ID of the customer to retrieve
     * @return APIResponse containing the CustomerResponse for the given ID
     */
    @GetMapping("/{customerId}")
    public APIResponse<CustomerResponse> getCustomerById(@PathVariable("customerId") Long customerId) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.getCustomerById(customerId))
                .build();
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId ID of the customer to delete
     * @return APIResponse with a success message
     */
    @DeleteMapping("/{customerId}")
    public APIResponse<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return APIResponse.<Void>builder()
                .message("Customer deleted successfully")
                .build();
    }

    /**
     * Retrieves a customer by their account ID.
     *
     * @param accountId Account ID associated with the customer
     * @return APIResponse containing the CustomerResponse for the given account ID
     */
    @GetMapping("/account/{accountId}")
    public APIResponse<CustomerResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.getCustomerByAccountId(accountId))
                .build();
    }

    /**
     * Updates customer information for a given account ID.
     *
     * @param accountId        Account ID of the customer to update
     * @param customerRequest  Object containing updated customer information
     * @param files            List of image files to update (optional)
     * @return APIResponse containing the updated CustomerResponse
     */
    @PutMapping(value = "/account/{accountId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<CustomerResponse> updateCustomer(
            @PathVariable("accountId") Long accountId,
            @ModelAttribute CustomerUpdateRequest customerRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return APIResponse.<CustomerResponse>builder()
                .data(customerService.updateCustomer(accountId, customerRequest, files))
                .build();
    }
}
