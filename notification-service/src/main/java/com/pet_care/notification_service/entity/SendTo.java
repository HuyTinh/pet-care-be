package com.pet_care.notification_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter // Generates getter methods for all fields
@Setter // Generates setter methods for all fields
@SuperBuilder // Provides a builder pattern for constructing the object, allowing for easier object creation
@ToString // Generates a toString method to display a string representation of the object
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
public class SendTo {

    @JsonProperty("to_email") // Maps this field to the "to_email" property in JSON when serialized/deserialized
    String toEmail; // The email address to which the message is being sent

    @JsonProperty("first_name") // Maps this field to the "first_name" property in JSON when serialized/deserialized
    String firstName; // The first name of the recipient

    @JsonProperty("last_name") // Maps this field to the "last_name" property in JSON when serialized/deserialized
    String lastName; // The last name of the recipient
}
