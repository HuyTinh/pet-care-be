package com.pet_care.employee_service.mapper;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.request.EmployeeUpdateRequest;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.model.Employee;
import org.mapstruct.*;

/**
 * Mapper interface for converting between different object models.
 * This interface uses MapStruct to generate the implementation for mapping between
 * DTOs (Data Transfer Objects) and Entity models.
 *
 * - The mappings define how to convert request and response objects (DTOs) to and from entities.
 * - The @Mapper annotation configures MapStruct to generate the implementation of the mapping logic.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    /**
     * Converts an EmployeeCreateRequest DTO to an Employee entity.
     *
     * @param employeeCreateRequest The DTO object containing employee data for creation.
     * @return The corresponding Employee entity.
     */
    Employee toEntity(EmployeeCreateRequest employeeCreateRequest);

    /**
     * Converts an Employee entity to an EmployeeResponse DTO.
     *
     * @param account The Employee entity to be converted.
     * @return The corresponding EmployeeResponse DTO.
     */
    EmployeeResponse toDto(Employee account);

    /**
     * Converts an Employee entity back to an EmployeeCreateRequest DTO.
     * This may be used in cases such as updates or other transformations.
     *
     * @param employee The Employee entity to be converted.
     * @return The corresponding EmployeeCreateRequest DTO.
     */
    EmployeeCreateRequest toCustomerRequest(EmployeeCreateRequest employee);

    /**
     * Updates an existing Employee entity with the data from an EmployeeUpdateRequest DTO.
     * Only non-null fields from the update request will be used, thanks to the mapping strategy.
     *
     * @param employeeUpdateRequest The DTO containing the updated employee data.
     * @param account The Employee entity to be partially updated.
     * @return The updated Employee entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(EmployeeUpdateRequest employeeUpdateRequest, @MappingTarget Employee account);
}
