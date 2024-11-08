package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.ServiceCRUDResponse;
import com.pet_care.manager_service.dto.response.ServiceResponse;
import com.pet_care.manager_service.entity.Services;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceCRUDResponse toServiceCRUDResponse(Services service);
}
