package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.CustomerPetAndServiceResponse;
import com.pet_care.manager_service.entity.Customer;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface CustomerMapper {

    CustomerPetAndServiceResponse toCustomerResponse(Customer customer);
}
