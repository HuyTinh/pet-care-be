package com.pet_care.medicine_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "medicines")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    Date manufaturingDate;

    Date expiryDate;

    Integer quantity;

    Double price;

    String note;

    Boolean status;
}
