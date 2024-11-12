package com.pet_care.appointment_service.model;

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
@Entity(name = "pets") // Marks this class as a JPA entity and specifies the table name as "pets"
@FieldDefaults(level = AccessLevel.PRIVATE) // Makes all fields private by default (for better encapsulation)
public class Pet {

    @Id // Marks this field as the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates the ID field (e.g., auto-increment in DB)
    Long id; // Unique identifier for each pet

    String name; // Name of the pet

    String age; // Age of the pet (may be stored as a string to allow flexible formats)

    Double weight; // Weight of the pet in kilograms (or other unit)

    String species; // Species of the pet (e.g., Dog, Cat, etc.)

    @ManyToOne // Establishes a many-to-one relationship between Pet and Appointment entities
    @JoinColumn(name = "appointment_id") // Specifies the foreign key column that links to the Appointment entity
    Appointment appointment; // The appointment associated with the pet (could be null if not assigned)

    @CreationTimestamp // Automatically sets this field to the current timestamp when the entity is created
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date createdAt; // Timestamp for when the pet was created in the system

    @UpdateTimestamp // Automatically sets this field to the current timestamp whenever the entity is updated
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to store both date and time in the database
    Date updatedAt; // Timestamp for when the pet's data was last updated
}
