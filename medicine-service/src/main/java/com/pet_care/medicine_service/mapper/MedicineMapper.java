package com.pet_care.medicine_service.mapper;

// Importing necessary libraries for mapping
import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.entity.Medicine;
import org.mapstruct.*;

/**
 * Mapper interface for converting between Medicine entities and DTOs.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {

    /**
     * Converts a MedicineCreateRequest DTO to a Medicine entity.
     * This method excludes the mapping of certain fields to avoid circular dependencies or invalid associations.
     *
     * @param medicineCreateRequest the DTO containing data for creating a new Medicine entity
     * @return a Medicine entity populated with data from the DTO
     */
    @Mapping(target = "calculationUnits", ignore = true)  // Ignores the calculationUnits field to avoid mapping
    @Mapping(target = "locations", ignore = true)          // Ignores the locations field to avoid mapping
    @Mapping(target ="manufacture", ignore = true)         // Ignores the manufacture field to avoid mapping
    Medicine toEntity(MedicineCreateRequest medicineCreateRequest);

    /**
     * Converts a Medicine entity to a MedicineResponse DTO.
     * Maps the Medicine's manufacture field to the corresponding response DTO.
     *
     * @param medicine the Medicine entity to be converted
     * @return a MedicineResponse DTO populated with data from the Medicine entity
     */
    @Mapping(target = "manufacture", source = "manufacture")  // Maps the manufacture field from the entity to the DTO
    @Mapping(target = "image_url", source = "imageUrl")
    MedicineResponse toDto(Medicine medicine);

    /**
     * Partially updates a Medicine entity with the data from a MedicineUpdateRequest DTO.
     * This method ignores null values in the update request to avoid overwriting existing data unnecessarily.
     *
     * @param medicineUpdateRequest the DTO containing the update data
     * @param medicine the existing Medicine entity to be updated
     * @return a Medicine entity updated with the new values from the DTO
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)  // Ignores null values in the update
    @Mapping(target = "calculationUnits", ignore = true)  // Ignores calculationUnits during the update
    @Mapping(target = "manufacture", ignore = true)       // Ignores manufacture during the update
    @Mapping(target = "locations", ignore = true)        // Ignores locations during the update
    Medicine partialUpdate(MedicineUpdateRequest medicineUpdateRequest, @MappingTarget Medicine medicine);
}
