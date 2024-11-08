package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.CustomerPetAndServiceResponse;
import com.pet_care.manager_service.dto.response.PageableResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerPetAndServiceResponse> getAllCustomers();

    PageableResponse<CustomerPetAndServiceResponse> getAllCustomersTrue(String search_query,int page_number, int page_size);

    CustomerPetAndServiceResponse deleteCustomer(Long id);
}
