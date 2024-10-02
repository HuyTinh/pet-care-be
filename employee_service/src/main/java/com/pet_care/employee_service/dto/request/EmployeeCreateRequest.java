package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;

public record EmployeeCreateRequest(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String email,
        Gender gender,
        @JsonProperty("image_url")
        String imageUrl,
        String address,
        @JsonProperty("account_id")
        Long accountId) {
}
