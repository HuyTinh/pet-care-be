package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.CustomerPetAndServiceResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerPetAndServiceResponse> getAllCustomers();

    List<CustomerPetAndServiceResponse> getAllCustomersTrue();

    CustomerPetAndServiceResponse deleteCustomer(Long id);
}
