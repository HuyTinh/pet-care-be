package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequest {
    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("image_url")
    String imageUrl;

    String email;

    String address;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("phone_number")
    String phoneNumber;
}
