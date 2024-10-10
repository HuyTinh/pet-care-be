package com.pet_care.employee_service.mapper;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.request.EmployeeUpdateRequest;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.model.Employee;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    Employee toEntity(EmployeeCreateRequest employeeCreateRequest);

    EmployeeResponse toDto(Employee account);

    EmployeeCreateRequest toCustomerRequest(EmployeeCreateRequest employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(EmployeeUpdateRequest employeeUpdateRequest, @MappingTarget Employee account);
}