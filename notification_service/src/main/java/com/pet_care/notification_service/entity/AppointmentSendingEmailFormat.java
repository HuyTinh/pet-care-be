package com.pet_care.notification_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder // Inherits builder functionality from the parent class, useful for building complex objects
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
@ToString // Generates a toString method to print a string representation of the object
@FieldDefaults(level = AccessLevel.PRIVATE) // Sets all fields to private access by default (for better encapsulation)
public class AppointmentSendingEmailFormat extends SendTo {

    @JsonProperty("appointment_id") // Specifies the name of this field when serialized to/from JSON
    Long appointmentId; // The ID of the appointment associated with the email

    String subject; // Default subject for the email

    String content; // The content of the email, will be generated dynamically

    @JsonProperty("appointment_date") // Specifies the name of this field when serialized to/from JSON
    String appointmentDate; // The date of the appointment, formatted as a String

    @JsonProperty("appointment_time") // Specifies the name of this field when serialized to/from JSON
    String appointmentTime; // The time of the appointment, formatted as a String

    String templateId;

    // Method to generate the content of the email dynamically based on the properties
    public String getContent() {
        // Constructs the email content as a JSON string with appointment details and a QR code
        return "{ \"to\": [ { \"email\": \"" + this.toEmail + "\"} ], \"subject\": \"" + this.subject + "\", \"params\": { \"username\": \"" + this.firstName + " " + this.lastName + "\", \"appointment_date\": \"" + this.appointmentDate + "\", \"appointment_time\": \"" + this.appointmentTime + "\", \"appointment_qr\": \"https://api.qrserver.com/v1/create-qr-code/?size=64x64&data=" + this.appointmentId + "\" }, \"templateId\": "+ this.templateId +" }";
    }
}
