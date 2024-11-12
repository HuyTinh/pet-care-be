package com.pet_care.customer_service.mapper;

import com.pet_care.customer_service.dto.request.AppointmentCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.dto.request.CustomerUpdateRequest;
import com.pet_care.customer_service.dto.response.CustomerResponse;
import com.pet_care.customer_service.model.Customer;
import org.mapstruct.*;

/**
 * Mapper interface to map between different DTOs and the Customer entity.
 * It uses MapStruct to generate the implementation.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    /**
     * Converts a CustomerCreateRequest to a Customer entity.
     * @param customerRequest DTO to be converted
     * @return The corresponding Customer entity
     */
    Customer toEntity(CustomerCreateRequest customerRequest);

    /**
     * Converts an AppointmentCreateRequest to a Customer entity.
     * @param request DTO to be converted
     * @return The corresponding Customer entity
     */
    Customer toEntity(AppointmentCreateRequest request);

    /**
     * Converts a Customer entity to a CustomerResponse DTO.
     * @param customer The Customer entity to be converted
     * @return The corresponding CustomerResponse DTO
     */
    CustomerResponse toDto(Customer customer);

    /**
     * Partially updates a Customer entity using data from CustomerUpdateRequest.
     * The fields that are null in the request will not be updated in the entity.
     * @param customerRequest DTO containing the data to update
     * @param customer The Customer entity to be updated
     * @return The updated Customer entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerUpdateRequest customerRequest, @MappingTarget Customer customer);
}
