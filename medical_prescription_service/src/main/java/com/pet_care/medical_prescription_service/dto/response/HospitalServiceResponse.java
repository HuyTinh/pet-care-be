package com.pet_care.medical_prescription_service.dto.response;

import com.pet_care.medical_prescription_service.enums.HospitalServiceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalServiceResponse {
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    HospitalServiceStatus status;
}