package com.pet_care.customer_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * The Customer entity representing a customer in the system.
 * This entity is mapped to the "customers" table in the database.
 */
@Getter
@Setter
@Builder
@Entity(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {

    /**
     * Unique identifier for the customer.
     * This is the primary key in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * The first name of the customer.
     */
    String firstName;

    /**
     * The last name of the customer.
     */
    String lastName;

    /**
     * The phone number of the customer.
     */
    String phoneNumber;

    /**
     * The address of the customer.
     */
    String address;

    /**
     * The email of the customer.
     */
    String email;

    /**
     * The gender of the customer.
     */
    @Enumerated(EnumType.STRING)
    Gender gender;

    /**
     * The URL of the customer's image.
     */
    String imageUrl;

    /**
     * The birth date of the customer.
     */
    @Temporal(TemporalType.DATE)
    Date birthDate;

    /**
     * The account ID associated with the customer.
     */
    Long accountId;

    /**
     * The timestamp when the customer record was created.
     * This field is automatically populated by Hibernate.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    /**
     * The timestamp when the customer record was last updated.
     * This field is automatically populated by Hibernate.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
