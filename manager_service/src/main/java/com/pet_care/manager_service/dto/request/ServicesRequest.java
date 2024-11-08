package com.pet_care.manager_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesRequest {
    @NotNull(message = "Please Enter Name")
    String name;

    @NotNull(message = "Please Enter Price")
    Double price;

    Boolean status = true;

    Long service_type_id;
}
