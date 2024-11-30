package com.pet_care.appointment_service.entity;

import com.pet_care.appointment_service.enums.HospitalServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder // Generates a builder for constructing objects with optional fields
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
@Entity(name = "hospital_services") // Marks this class as a JPA entity and specifies the table name
@FieldDefaults(level = AccessLevel.PRIVATE) // Makes all fields private by default (for better encapsulation)
public class HospitalServiceEntity {

    @Id // Marks this field as the primary key of the entity
    String name; // Name of the hospital service, which is also the primary key

    String description; // A description of the hospital service

    @Enumerated(EnumType.STRING) // Specifies that the enum will be stored as a string in the database
    @Builder.Default // Sets the default value for the status field when using the builder
    HospitalServiceStatus status = HospitalServiceStatus.ACTIVE; // Status of the service, default is ACTIVE

    Double price; // Price of the hospital service

    @CreationTimestamp // Automatically sets this field to the current timestamp when the entity is created
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date createdAt; // Timestamp for when the hospital service was created

    @UpdateTimestamp // Automatically sets this field to the current timestamp whenever the entity is updated
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date updatedAt; // Timestamp for when the hospital service was last updated
}
