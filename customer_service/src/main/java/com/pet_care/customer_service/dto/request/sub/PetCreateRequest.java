package com.pet_care.customer_service.dto.request.sub;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetCreateRequest {
    Long id;

    String name;

    String age;

    Double weight;

    String species;
}
