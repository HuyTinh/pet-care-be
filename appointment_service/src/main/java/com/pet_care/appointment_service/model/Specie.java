package com.pet_care.appointment_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter // Generates getter methods for all fields
@Setter // Generates setter methods for all fields
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a no-argument constructor
@Entity(name = "species") // Marks this class as a JPA entity and maps it to the "species" table
public class Specie {

    @Id // Specifies that the "name" field is the primary key for this entity
    String name; // The name of the species (e.g., "Dog", "Cat")

    Boolean status; // Represents whether the species is active (true) or inactive (false)

    @CreationTimestamp // Automatically sets the creation timestamp when the entity is created
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to a timestamp column in the database
    Date createdAt; // Timestamp indicating when this record was created

    @UpdateTimestamp // Automatically sets the update timestamp when the entity is modified
    @Temporal(TemporalType.TIMESTAMP) // Maps the field to a timestamp column in the database
    Date updatedAt; // Timestamp indicating the last time this record was updated
}
