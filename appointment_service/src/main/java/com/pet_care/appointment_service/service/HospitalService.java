package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.dto.request.HospitalServiceRequest;
import com.pet_care.appointment_service.dto.response.HospitalServiceResponse;
import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.HospitalServiceMapper;
import com.pet_care.appointment_service.model.HospitalServiceEntity;
import com.pet_care.appointment_service.repository.HospitalServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indicates that this is a service class
@RequiredArgsConstructor // Lombok annotation to automatically create a constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok to set field access level to private and final
public class HospitalService {

    // Repositories and mappers for handling hospital services
    HospitalServiceRepository hospitalServiceRepository;
    HospitalServiceMapper hospitalServiceMapper;

    /**
     * Retrieves all hospital services.
     * @return List of all hospital service responses.
     */
    @Transactional(readOnly = true) // Marks this method as a read-only transaction
    public List<HospitalServiceResponse> getAllHospitalService() {
        // Fetch all hospital service entities and map them to DTOs
        List<HospitalServiceEntity> hospitalServices = hospitalServiceRepository.findAll();
        return hospitalServices.stream().map(hospitalServiceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a hospital service by its name.
     * @param name The name of the hospital service.
     * @return A response DTO of the hospital service.
     * @throws APIException If the hospital service is not found.
     */
    @Transactional(readOnly = true) // Mark as read-only for performance
    public HospitalServiceResponse getHospitalServiceById(String name) {
        // Find the hospital service by name and handle case where it is not found
        HospitalServiceEntity hospitalServiceEntity = hospitalServiceRepository
                .findById(name)
                .orElseThrow(() -> new APIException(ErrorCode.HOSPITAL_SERVICE_NOT_FOUND));
        return hospitalServiceMapper.toDto(hospitalServiceEntity);
    }

    /**
     * Creates a new hospital service.
     * @param hospitalServiceRequest The data required to create the service.
     * @return A response DTO of the created hospital service.
     */
    public HospitalServiceResponse createHospitalService(HospitalServiceRequest hospitalServiceRequest) {
        // Convert the incoming request to an entity and save it
        HospitalServiceEntity hospitalService = hospitalServiceMapper.toEntity(hospitalServiceRequest);
        // Persist the hospital service and return the response DTO
        return hospitalServiceMapper.toDto(hospitalServiceRepository.save(hospitalService));
    }

    /**
     * Updates an existing hospital service.
     * @param hospitalService The name of the service to be updated.
     * @param hospitalServiceRequest The request data for updating.
     * @return A response DTO of the updated hospital service.
     * @throws APIException If the hospital service is not found.
     */
    public HospitalServiceResponse updateHospitalService(String hospitalService, HospitalServiceRequest hospitalServiceRequest) {
        // Find the existing service to update
        HospitalServiceEntity existHospitalServiceEntity = hospitalServiceRepository
                .findById(hospitalService)
                .orElseThrow(() -> new APIException(ErrorCode.HOSPITAL_SERVICE_NOT_FOUND));

        // Perform partial update and save the service
        HospitalServiceEntity updatedHospitalServiceEntity = hospitalServiceMapper.partialUpdate(hospitalServiceRequest, existHospitalServiceEntity);
        return hospitalServiceMapper.toDto(hospitalServiceRepository.save(updatedHospitalServiceEntity));
    }

    /**
     * Deletes a hospital service by its name.
     * @param hospitalService The name of the service to delete.
     */
    public void deleteHospitalService(String hospitalService) {
        // Delete the hospital service from the repository
        hospitalServiceRepository.deleteById(hospitalService);
    }
}
