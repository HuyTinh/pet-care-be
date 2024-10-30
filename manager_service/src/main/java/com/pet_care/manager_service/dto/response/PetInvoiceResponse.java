package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetInvoiceResponse {
    Long id;
    String name;
    String age;
    double weight;
    SpeciesResponse speciesResponse;
    CustomerPrescriptionResponse customerResponse;
}
