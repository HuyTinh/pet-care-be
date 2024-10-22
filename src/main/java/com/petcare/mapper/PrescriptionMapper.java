package com.petcare.mapper;

import com.petcare.entity.Appointment;
import com.petcare.entity.Pet;
import com.petcare.entity.Prescription;
import com.petcare.entity.PrescriptionDetails;
import com.petcare.dto.response.PetResponse;
import com.petcare.dto.response.PrescriptionDetailsResponse;
import com.petcare.dto.response.PrescriptionResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(uses = PrescriptionDetailMapper.class)
public interface PrescriptionMapper {

    PrescriptionMapper INSTANCE = Mappers.getMapper(PrescriptionMapper.class);

    @Mapping(source = "id", target = "prescriptionId")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "prescriptionDetails", target = "prescriptionDetailResponse")
    PrescriptionResponse toPrescriptionResponse(Prescription prescription);
    List<PrescriptionResponse> mapPrescriptionToPrescriptionResponse(List<Prescription> prescription);

    @AfterMapping
    default void calculateTotalPrice(Prescription prescription, @MappingTarget PrescriptionResponse response) {
        // Ánh xạ PrescriptionDetails sang PrescriptionDetailsResponse
        List<PrescriptionDetailsResponse> prescriptionDetailResponse = PrescriptionDetailMapper.INSTANCE.mapPrescriptionDetailToPrescriptionDetailsResponse(prescription.getPrescriptionDetails());

        // Tính tổng totalPrice từ PrescriptionDetailResponse
        double totalPrice = prescriptionDetailResponse.stream()
                .mapToDouble(PrescriptionDetailsResponse::getTotalPriceInPrescriptionDetail)
                .sum();

        // Gán tổng giá trị totalPrice vào PrescriptionResponse
        response.setTotalPriceInPrescription(totalPrice);
        response.setPrescriptionDetailResponse(prescriptionDetailResponse);
    }

}


