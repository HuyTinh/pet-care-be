package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {
    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @JsonProperty("image_url")
    String imageUrl;
}
