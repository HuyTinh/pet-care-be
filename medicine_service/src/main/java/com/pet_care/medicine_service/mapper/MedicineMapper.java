package com.pet_care.medicine_service.mapper;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.model.Medicine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {
    @Mapping(target = "calculationUnits", ignore = true)
    @Mapping(target = "manufactures", ignore = true)
    @Mapping(target = "locations", ignore = true)
    Medicine toEntity(MedicineCreateRequest medicineCreateRequest);

    MedicineResponse toDto(Medicine medicine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Medicine partialUpdate(MedicineUpdateRequest medicineUpdateRequest, @MappingTarget Medicine medicine);
}
