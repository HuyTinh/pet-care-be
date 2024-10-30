package com.pet_care.manager_service.dto.response;

import com.pet_care.manager_service.entity.Medicine;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceMedicineDetailResponse {
    Long id;
    MedicineResponse medicineResponse;
    Integer quantity;
    Double price;
    String note;
}
