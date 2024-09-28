package com.pet_care.appointment_service.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {
    Long id;

    @JsonProperty("first_name")
    String first_name;
    @JsonProperty("last_name")
    String last_name;
    @JsonProperty("phone_number")
    String phone_number;

    String address;

    String email;

    @JsonProperty("account_id")
    Long accountId;

    Set<PetResponse> pets;
}
