package com.pet_care.medicine_service.model;

// Import necessary libraries
import com.fasterxml.jackson.annotation.JsonIgnore;  // Jackson annotation to ignore this field in JSON serialization
import jakarta.persistence.*;  // JPA annotations for entity management
import lombok.*;  // Lombok annotations for boilerplate code reduction
import lombok.experimental.FieldDefaults;  // Lombok annotation for controlling field visibility

import java.util.Set;

/**
 * The Manufacture entity represents a manufacturer or producer of medicines or other medical products.
 * This entity is mapped to the "manufactures" table in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "manufactures")  // Specifies the name of the table in the database
@FieldDefaults(level = AccessLevel.PRIVATE)  // Ensures that all fields have private access by default
public class Manufacture {

    /**
     * Unique identifier for the Manufacture entity.
     * The ID is automatically generated using an auto-increment strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // The ID is auto-generated and incremented
            Long id;

    /**
     * The name of the manufacturer or producer.
     */
    String name;

    /**
     * The status of the manufacturer, indicating whether the manufacturer is active or inactive.
     */
    Boolean status;

    /**
     * A one-to-many relationship between Manufacture and Medicine.
     * This means a manufacturer can produce multiple medicines, and each medicine has one manufacturer.
     * The `mappedBy` attribute specifies the field in the Medicine entity that maps to this relationship.
     * CascadeType.ALL ensures that all operations (e.g., persist, delete) on the manufacturer will be cascaded to the medicines.
     * orphanRemoval = true ensures that if a medicine is removed from the set, it will be deleted from the database.
     * The @JsonIgnore annotation prevents the medicines field from being serialized to JSON, avoiding a circular reference.
     */
    @OneToMany(mappedBy = "manufacture", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Prevents serialization of the medicines field into JSON
    private Set<Medicine> medicines;
}
