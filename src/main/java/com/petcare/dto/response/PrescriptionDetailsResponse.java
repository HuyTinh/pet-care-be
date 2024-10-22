package com.petcare.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionDetailsResponse {

    long medicineId;

    String medicineName;

    Object medicineUnit;

    int medicineQuantity;

    double totalPriceInPrescriptionDetail;

//    String note;
}
