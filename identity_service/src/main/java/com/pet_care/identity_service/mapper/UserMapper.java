package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.UserCreationRequest;
import com.pet_care.identity_service.dto.request.UserUpdateRequest;
import com.pet_care.identity_service.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserCreationRequest userCreationRequest);

    UserCreationRequest toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserUpdateRequest userCreationRequest, @MappingTarget User user);
}