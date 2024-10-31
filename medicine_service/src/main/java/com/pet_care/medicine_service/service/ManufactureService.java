package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.ManufactureMapper;
import com.pet_care.medicine_service.repository.ManufactureRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufactureService {

    ManufactureRepository manufactureRepository;

    ManufactureMapper manufactureMapper;

    /**
     * @return
     */
    public List<ManufactureResponse> getAllManufacture() {
        List<ManufactureResponse> manufactureResponseList = manufactureRepository.findAll().stream().map(manufactureMapper::toDto).toList();

        log.info("Manufacture List: {}", manufactureResponseList);

        return manufactureResponseList;
    }

    /**
     * @param id
     * @return
     */
    public ManufactureResponse getManufactureById(Long id) {
        ManufactureResponse manufactureResponse = manufactureMapper.toDto(manufactureRepository.findById(id).orElseThrow(() -> new APIException(ErrorCode.MANUFACTURE_NOT_FOUND)));

        log.info("Manufacture Response: {}", manufactureResponse);

        return manufactureResponse;
    }


}
