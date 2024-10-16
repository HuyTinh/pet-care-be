package com.pet_care.medical_prescription_service.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pet {

    Long id;

    String name;

    String age;

    String weight;

    String specie;
}
