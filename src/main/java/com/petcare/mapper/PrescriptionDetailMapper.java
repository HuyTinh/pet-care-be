package com.petcare.mapper;

import com.petcare.entity.PrescriptionDetails;
import com.petcare.dto.response.PrescriptionDetailsResponse;
import com.petcare.dto.response.PrescriptionResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface PrescriptionDetailMapper
{

    PrescriptionDetailMapper INSTANCE = Mappers.getMapper(PrescriptionDetailMapper.class);

    @Mapping(source = "medicine.id", target = "medicineId")
    @Mapping(source = "medicine.name", target = "medicineName")
    @Mapping(source = "quantity", target = "medicineQuantity")
    @Mapping(source = "medicine.caculationUnits.name", target = "medicineUnit")
    PrescriptionDetailsResponse mapPrescriptionDetails(PrescriptionDetails prescriptionDetails);
    List<PrescriptionDetailsResponse> mapPrescriptionDetailToPrescriptionDetailsResponse(List<PrescriptionDetails> prescriptionDetails);

    @AfterMapping
    default void calculateTotalPriceInPrescriptionDetail(PrescriptionDetails prescriptionDetail, @MappingTarget PrescriptionDetailsResponse response) {
        double totalPrice = prescriptionDetail.getQuantity() * prescriptionDetail.getMedicine().getPrice();
        response.setTotalPriceInPrescriptionDetail(totalPrice);  // Gán giá trị tổng vào response


    }

}
