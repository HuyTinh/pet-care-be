package com.pet_care.appointment_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "appointments") // Marks the class as an entity to be mapped to the "appointments" table in the database
@FieldDefaults(level = AccessLevel.PRIVATE) // Makes all fields private by default (for better encapsulation)
public class Appointment {

    @Id // Specifies that this field is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates the ID value (auto-increment)
    Long id; // The ID of the appointment

    @JsonProperty("account_id") // Specifies the name of this field when serialized to/from JSON
    Long accountId; // Account ID associated with the appointment

    @JsonProperty("first_name") // Specifies the name of this field when serialized to/from JSON
    String firstName; // First name of the customer

    @JsonProperty("last_name") // Specifies the name of this field when serialized to/from JSON
    String lastName; // Last name of the customer

    @JsonProperty("email") // Specifies the name of this field when serialized to/from JSON
    String email; // Email address of the customer

    @JsonProperty("phone_number") // Specifies the name of this field when serialized to/from JSON
    String phoneNumber; // Phone number of the customer

    @JsonProperty("appointment_date") // Specifies the name of this field when serialized to/from JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Customizes the date format for JSON
    @Temporal(TemporalType.DATE) // Maps the field to store only the date part (not time) in the database
    Date appointmentDate; // The date of the appointment

    @JsonProperty("appointment_time") // Specifies the name of this field when serialized to/from JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Customizes the time format for JSON
    @Temporal(TemporalType.TIME) // Maps the field to store only the time part (not date) in the database
    Date appointmentTime; // The time of the appointment

    @ManyToMany(fetch = FetchType.EAGER) // Defines a many-to-many relationship with the HospitalServiceEntity
    Set<HospitalServiceEntity> services; // Set of services associated with the appointment

    @Enumerated(EnumType.STRING) // Specifies that the enum value should be stored as a string in the database
    AppointmentStatus status; // The status of the appointment (e.g., scheduled, completed)

    @OneToMany(mappedBy="appointment")
    Set<FollowUp> followUps;

    @CreationTimestamp // Automatically sets this field to the current timestamp when the entity is created
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date createdAt; // The creation timestamp of the appointment

    @UpdateTimestamp // Automatically sets this field to the current timestamp whenever the entity is updated
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date updatedAt; // The last update timestamp of the appointment
}
