package com.pet_care.appointment_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@SuperBuilder // Inherits builder functionality from the parent class, useful for building complex objects
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
@ToString // Generates a toString method to print a string representation of the object
@FieldDefaults(level = AccessLevel.PRIVATE) // Sets all fields to private access by default (for better encapsulation)
public class EmailBookingSuccessful extends SendTo {

    @JsonProperty("appointment_id") // Specifies the name of this field when serialized to/from JSON
    Long appointmentId; // The ID of the appointment associated with the email

    @Builder.Default // Provides a default value for this field when using the builder
    String subject = "Appointment Confirmation!"; // Default subject for the email

    String content; // The content of the email, will be generated dynamically

    @JsonProperty("appointment_date") // Specifies the name of this field when serialized to/from JSON
    String appointmentDate; // The date of the appointment, formatted as a String

    @JsonProperty("appointment_time") // Specifies the name of this field when serialized to/from JSON
    String appointmentTime; // The time of the appointment, formatted as a String

    @CreationTimestamp // Automatically sets this field to the current timestamp when the entity is created
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date createdAt; // Timestamp for when the email booking was created

    @UpdateTimestamp // Automatically sets this field to the current timestamp whenever the entity is updated
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date updatedAt; // Timestamp for when the email booking was last updated

    // Method to generate the content of the email dynamically based on the properties
    public String getContent() {
        // Constructs the email content as a JSON string with appointment details and a QR code
        return "{ \"to\": [ { \"email\": \"" + this.toEmail + "\"} ], \"subject\": \"" + this.subject + "\", \"params\": { \"username\": \"" + this.firstName + " " + this.lastName + "\", \"appointment_date\": \"" + this.appointmentDate + "\", \"appointment_time\": \"" + this.appointmentTime + "\", \"appointment_qr\": \"https://api.qrserver.com/v1/create-qr-code/?size=64x64&data=" + this.appointmentId + "\" }, \"templateId\": 1 }";
    }
}
