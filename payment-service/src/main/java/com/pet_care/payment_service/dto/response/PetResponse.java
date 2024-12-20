package com.pet_care.payment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {
    Long id;
    String name;
    String age;
    Double weight;
    String species;
}
