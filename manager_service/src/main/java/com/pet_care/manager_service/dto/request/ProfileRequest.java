package com.pet_care.manager_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileRequest {
    @NotNull(message = "Please Enter Your First Name")
    String first_name;

    @NotNull(message = "Please Enter Your Last Name")
    String last_name;

}
