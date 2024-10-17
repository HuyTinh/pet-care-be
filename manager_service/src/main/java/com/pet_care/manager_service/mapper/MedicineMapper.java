package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.MedicineResponse;
import com.pet_care.manager_service.entity.Medicine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    MedicineResponse toMedicineResponse(Medicine medicine);
}
