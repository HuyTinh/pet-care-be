package com.pet_care.medicine_service.entity;

// Importing necessary libraries
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * The CalculationUnit entity represents a unit of calculation for a product,
 * such as quantity units for medicines or surgical instruments.
 * This entity is mapped to the "calculation_units" table in the database.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "calculation_units")  // Specifies the name of the table in the database
public class CalculationUnit {

    /**
     * Unique identifier for the CalculationUnit entity.
     * The ID is automatically generated using an auto-increment strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // The ID is auto-generated and incremented
            Long id;

    /**
     * The name of the calculation unit, such as "box", "tablet", etc.
     */
    String name;

    /**
     * The status of the calculation unit, representing whether it's active or inactive.
     */
    Boolean status;

    /**
     * The timestamp indicating when this CalculationUnit was created.
     * This value is automatically set to the current time when the entity is created.
     */
    @Temporal(TemporalType.TIMESTAMP)  // Specifies the timestamp format
    @CreationTimestamp  // Automatically sets the value when the entity is created
            Date createdAt;

    /**
     * The timestamp indicating when this CalculationUnit was last updated.
     * This value is automatically set to the current time when the entity is updated.
     */
    @Temporal(TemporalType.TIMESTAMP)  // Specifies the timestamp format
    @UpdateTimestamp  // Automatically sets the value when the entity is updated
            Date updatedAt;
}
