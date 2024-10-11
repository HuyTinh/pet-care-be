package com.pet_care.customer_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.customer_service.client.UploadImageClient;
import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.request.sub.AppointmentRequest;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.exception.APIException;
import com.pet_care.customer_service.exception.ErrorCode;
import com.pet_care.customer_service.mapper.CustomerMapper;
import com.pet_care.customer_service.model.Customer;
import com.pet_care.customer_service.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    @NotNull CustomerRepository customerRepository;

    @NotNull CustomerMapper customerMapper;

    @NotNull MessageService messageService;

    @NotNull ObjectMapper objectMapper;

    @NotNull UploadImageClient uploadImageClient;

    @NotNull
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(@NotNull Long id) {
        return customerRepository.findById(id).map(customerMapper::toDto).orElseThrow(() -> new RuntimeException(("")));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId).map(customerMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.EMAIL_NOT_FOUND));
    }

    @JmsListener(destination = "customer-create-queue")
    public void addCustomerFromMessageQueue(String customerRequest) throws JsonProcessingException {
        customerRepository.save(customerMapper.toEntity(objectMapper.readValue(customerRequest, CustomerCreateRequest.class)));
    }

    public CustomerResponse createAppointment(@NotNull AppointmentCreateRequest request, Boolean notification) throws JsonProcessingException {
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

    public void deleteCustomer(@NotNull Long id) {
        customerRepository.deleteById(id);
    }
}
