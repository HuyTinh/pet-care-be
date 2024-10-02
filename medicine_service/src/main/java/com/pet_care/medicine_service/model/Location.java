package com.pet_care.medicine_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String area;

    Integer rowLocation;

    Integer columnLocation;

    Boolean status;
}
