package com.pet_care.medicine_service.model;

// Import necessary libraries
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
/**
 * The Location entity represents a physical location where a product can be stored or found.
 * This entity is mapped to the "locations" table in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "locations")  // Specifies the name of the table in the database
@FieldDefaults(level = AccessLevel.PRIVATE)  // Ensures that all fields have private access by default
public class Location {

    /**
     * Unique identifier for the Location entity.
     * The ID is automatically generated using an auto-increment strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // The ID is auto-generated and incremented
            Long id;

    /**
     * The area or description of the location, such as "warehouse 1", "aisle B", etc.
     */
    String area;

    /**
     * The row number of the location, often used in grid-based locations like shelves or sections.
     */
    @JsonProperty("row_location")  // Customizes the JSON property name for this field
            Integer rowLocation;

    /**
     * The column number of the location, often used in grid-based locations like shelves or sections.
     */
    @JsonProperty("column_location")  // Customizes the JSON property name for this field
            Integer columnLocation;

    /**
     * The status of the location, indicating whether it is active or inactive.
     */
    Boolean status;

    /**
     * The timestamp indicating when this Location was created.
     * This value is automatically set to the current time when the entity is created.
     */
    @Temporal(TemporalType.TIMESTAMP)  // Specifies the timestamp format
    @CreationTimestamp  // Automatically sets the value when the entity is created
            Date createdAt;

    /**
     * The timestamp indicating when this Location was last updated.
     * This value is automatically set to the current time when the entity is updated.
     */
    @Temporal(TemporalType.TIMESTAMP)  // Specifies the timestamp format
    @UpdateTimestamp  // Automatically sets the value when the entity is updated
            Date updatedAt;
}
