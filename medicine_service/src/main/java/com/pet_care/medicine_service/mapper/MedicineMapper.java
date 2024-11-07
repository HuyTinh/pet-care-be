package com.pet_care.medicine_service.mapper;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.model.Medicine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {
    /**
     * @param medicineCreateRequest
     * @return
     */
    @Mapping(target = "calculationUnits", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target ="manufacture", ignore = true)
    Medicine toEntity(MedicineCreateRequest medicineCreateRequest);

    /**
     * @param medicine
     * @return
     */
    @Mapping(target = "manufacture", source = "manufacture")
    MedicineResponse toDto(Medicine medicine);

    /**
     * @param medicineUpdateRequest
     * @param medicine
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "calculationUnits", ignore = true)
    @Mapping(target = "manufacture", ignore = true)
    @Mapping(target = "locations", ignore = true)
    Medicine partialUpdate(MedicineUpdateRequest medicineUpdateRequest, @MappingTarget Medicine medicine);
}
