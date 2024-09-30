package com.pet_care.customer_service.mapper;

import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.model.Customer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequest customerRequest);

    Customer toEntity(AppointmentCreateRequest request);


    CustomerResponse toDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerCreateRequest customerRequest, @MappingTarget Customer customer);
}