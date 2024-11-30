package com.pet_care.customer_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.customer_service.client.UploadImageClient;
import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.request.AppointmentRequest;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.exception.APIException;
import com.pet_care.customer_service.exception.ErrorCode;
import com.pet_care.customer_service.mapper.CustomerMapper;
import com.pet_care.customer_service.entity.Customer;
import com.pet_care.customer_service.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling customer-related operations.
 * It includes functionality for managing customer data, appointments, and updates.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {

    // Repository for accessing customer data in the database
    CustomerRepository customerRepository;

    // Mapper to convert between entity and DTO objects
    CustomerMapper customerMapper;

    // Service to send messages for customer-related tasks
    MessageService messageService;

    // ObjectMapper to process JSON data
    ObjectMapper objectMapper;

    // Client for uploading images
    UploadImageClient uploadImageClient;

    /**
     * Fetches all customers and returns them as a list of CustomerResponse DTOs.
     *
     * @return a list of CustomerResponse DTOs representing all customers
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Fetches a customer by ID and returns their data as a CustomerResponse DTO.
     *
     * @param id the ID of the customer
     * @return a CustomerResponse DTO containing customer data
     * @throws APIException if the customer with the specified ID is not found
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    /**
     * Fetches a customer by their account ID and returns their data as a CustomerResponse DTO.
     *
     * @param accountId the account ID of the customer
     * @return a CustomerResponse DTO containing customer data
     * @throws APIException if the customer with the specified account ID is not found
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId).map(customerMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_FOUND));
    }

    /**
     * Creates an appointment for a customer and sends a notification if requested.
     *
     * @param request the AppointmentCreateRequest DTO containing appointment details
     * @param notification whether a notification should be sent
     * @return a CustomerResponse DTO containing the updated customer data
     * @throws JsonProcessingException if there is an error processing JSON data
     */
    @Transactional
    public CustomerResponse createAppointment(AppointmentCreateRequest request, Boolean notification) throws JsonProcessingException {
        Customer customerSave = customerRepository.findByAccountId(request.getAccountId()).orElse(null);

        if (customerSave == null) {
            customerSave = customerRepository.save(customerMapper.toEntity(request));
        }

        AppointmentRequest appointment = request.getAppointment();
        appointment.setCustomerId(customerSave.getId());

        String notify = "";
        if (notification) {
            notify = "-with-notification";
        }

        messageService.sendMessageQueue("customer-create-appointment" + notify + "-queue", objectMapper.writeValueAsString(appointment));

        return customerMapper.toDto(customerRepository.save(customerSave));
    }

    /**
     * Processes a message from the queue and adds a new customer to the repository.
     *
     * @param customerRequest the customer data received from the queue
     * @throws JsonProcessingException if there is an error processing JSON data
     */
    @JmsListener(destination = "customer-create-queue")
    @Transactional
    public void addCustomerFromMessageQueue(String customerRequest) throws JsonProcessingException {
        customerRepository.save(customerMapper.toEntity(objectMapper.readValue(customerRequest, CustomerCreateRequest.class)));
    }

    /**
     * Updates an existing customer's information and uploads a new profile image if provided.
     *
     * @param accountId the account ID of the customer to update
     * @param customerRequest the updated customer data
     * @param files optional files to upload (e.g., profile image)
     * @return a CustomerResponse DTO containing the updated customer data
     */
    @Transactional
    public CustomerResponse updateCustomer(Long accountId, CustomerUpdateRequest customerRequest, @Nullable List<MultipartFile> files) {
        Customer existingCustomer = customerRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new APIException(ErrorCode.CUSTOMER_NOT_FOUND));

        customerMapper.partialUpdate(customerRequest, existingCustomer);

        if (files != null && !files.isEmpty()) {
            existingCustomer.setImageUrl(uploadImageClient.uploadImage(files).get(0));
        }

        return customerMapper.toDto(customerRepository.save(existingCustomer));
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     */
    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
