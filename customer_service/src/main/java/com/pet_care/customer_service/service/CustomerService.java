package com.pet_care.customer_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.request.sub.Appointment;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.exception.CustomerException;
import com.pet_care.customer_service.exception.ErrorCode;
import com.pet_care.customer_service.mapper.CustomerMapper;
import com.pet_care.customer_service.model.Customer;
import com.pet_care.customer_service.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;

    CustomerMapper customerMapper;

    MessageService messageService;

    ObjectMapper objectMapper;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDto).orElseThrow(() -> new RuntimeException(("")));
    }

    public CustomerResponse getCustomerByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId).map(customerMapper::toDto).orElseThrow(() -> new CustomerException(ErrorCode.EMAIL_NOT_FOUND));
    }

//    public CustomerResponse addCustomer(CustomerRequest customerRequest) throws JsonProcessingException {
//        Customer customerSave = customerRepository.save(customerMapper.toEntity(customerRequest));
//        return customerMapper.toDto(customerRepository.save(customerSave));
//    }

    @JmsListener(destination = "customer-create-queue")
    public void addCustomerFromMessageQueue(String customerRequest) throws JsonProcessingException {
        customerRepository.save(customerMapper.toEntity(objectMapper.readValue(customerRequest, CustomerCreateRequest.class)));
    }

    public CustomerResponse createAppointment(AppointmentCreateRequest request, Boolean notification) throws JsonProcessingException {
        Customer customerSave = customerRepository.findByAccountId(request.getAccountId()).orElse(null);

        if(customerSave == null) {
            customerSave = customerRepository.save(customerMapper.toEntity(request));
        }

        Appointment appointment = request.getAppointment();

        appointment.setCustomerId(customerSave.getId());

        String notify = "";
        if(notification){
            notify = "-with-notification";
        }

        System.out.println(objectMapper.writeValueAsString(appointment));

        messageService.sendMessageQueue("customer-create-appointment"+notify+"-queue", objectMapper.writeValueAsString(appointment));


        return customerMapper.toDto(customerRepository.save(customerSave));
    }


    public CustomerResponse updateCustomer(Long accountId, CustomerCreateRequest customerRequest) {
        Customer existingCustomer = customerRepository.findByAccountId(accountId).orElseThrow(() -> new CustomerException(ErrorCode.CUSTOMER_NOT_FOUND));
        customerMapper.partialUpdate(customerRequest, existingCustomer);
        return customerMapper.toDto(customerRepository.save(existingCustomer));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
