package com.pet_care.employee_service.mapper;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.model.Employee;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    Employee toEntity(EmployeeCreateRequest employeeRequest);

    EmployeeResponse toDto(Employee employee);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(EmployeeCreateRequest employeeRequest, @MappingTarget Employee employee);
}