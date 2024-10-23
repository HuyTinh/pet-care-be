package com.pet_care.medical_prescription_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalServiceResponse {
    String name;

    String description;

    Double price;
}
