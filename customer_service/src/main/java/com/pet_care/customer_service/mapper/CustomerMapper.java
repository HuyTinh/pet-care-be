package com.pet_care.customer_service.mapper;

import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.model.Customer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    /**
     * @param customerRequest
     * @return
     */
    Customer toEntity(CustomerCreateRequest customerRequest);

    /**
     * @param request
     * @return
     */
    Customer toEntity(AppointmentCreateRequest request);

    /**
     * @param customer
     * @return
     */
    CustomerResponse toDto(Customer customer);

    /**
     * @param customerRequest
     * @param customer
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerUpdateRequest customerRequest, @MappingTarget Customer customer);
}