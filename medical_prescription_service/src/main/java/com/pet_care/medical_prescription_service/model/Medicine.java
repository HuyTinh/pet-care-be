package com.pet_care.medical_prescription_service.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    Long id;

    String name;

    Integer quantity;

    Double price;
}
