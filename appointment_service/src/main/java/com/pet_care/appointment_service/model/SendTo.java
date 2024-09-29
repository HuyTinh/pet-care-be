package com.pet_care.appointment_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SendTo {
    @JsonProperty("to_email")
    String toEmail;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;
}
