package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class PrescriptionResponse {
    Long id;
    LocalDate create_date;
    String note;
    Set<PrescriptionDetailResponse> prescriptionDetailResponse;
}
