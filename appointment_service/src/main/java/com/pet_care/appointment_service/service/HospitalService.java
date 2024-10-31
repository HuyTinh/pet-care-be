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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalService {

     HospitalServiceRepository hospitalServiceRepository;

     HospitalServiceMapper hospitalServiceMapper;

    /**
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<HospitalServiceResponse> getAllHospitalService() {
        List<HospitalServiceEntity> hospitalServices = hospitalServiceRepository.findAll();
        return hospitalServices.stream().map(hospitalServiceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public HospitalServiceResponse getHospitalServiceById( String name) {
        HospitalServiceEntity hospitalServiceEntity = hospitalServiceRepository
                .findById(name)
                .orElseThrow(() -> new APIException(ErrorCode.HOSPITAL_SERVICE_NOT_FOUND));
        return hospitalServiceMapper.toDto(hospitalServiceEntity);
    }

    /**
     * @param hospitalServiceRequest
     * @return
     */
    public HospitalServiceResponse createHospitalService(HospitalServiceRequest hospitalServiceRequest) {
        HospitalServiceEntity hospitalService = hospitalServiceMapper.toEntity(hospitalServiceRequest);
        System.out.println(hospitalService);
        return hospitalServiceMapper.toDto(hospitalServiceRepository.save(hospitalService));
    }

    /**
     * @param hospitalService
     * @param hospitalServiceRequest
     * @return
     */
    public HospitalServiceResponse updateHospitalService( String hospitalService, HospitalServiceRequest hospitalServiceRequest) {
        HospitalServiceEntity existHospitalServiceEntity = hospitalServiceRepository
                .findById(hospitalService)
                .orElseThrow(() -> new APIException(ErrorCode.HOSPITAL_SERVICE_NOT_FOUND));

        HospitalServiceEntity updatedHospitalServiceEntity = hospitalServiceMapper.partialUpdate(hospitalServiceRequest, existHospitalServiceEntity);
        return hospitalServiceMapper.toDto(hospitalServiceRepository.save(updatedHospitalServiceEntity));
    }

    /**
     * @param hospitalService
     */
    public void deleteHospitalService( String hospitalService) {
        hospitalServiceRepository.deleteById(hospitalService);
    }
}
