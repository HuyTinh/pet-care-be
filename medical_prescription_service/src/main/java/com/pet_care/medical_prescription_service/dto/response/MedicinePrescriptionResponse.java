package com.pet_care.medical_prescription_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicinePrescriptionResponse {
    Long id;
    String name;
    Long quantity;
    String calculateUnit;
}
