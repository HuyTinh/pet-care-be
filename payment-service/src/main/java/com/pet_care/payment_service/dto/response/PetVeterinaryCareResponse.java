package com.pet_care.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetVeterinaryCareResponse {
    Long id;

    @JsonProperty("veterinary_care")
    String veterinaryCare;

    String result;

    @JsonProperty("total_money")
    Double totalMoney;
}
