package com.pet_care.medicine_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

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

    @JsonProperty("manufacturing_date")
    Date manufacturingDate;

    @JsonProperty("expiry_date")
    Date expiryDate;

    Integer quantity;

    Double price;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    Set<CalculationUnit> calculationUnits;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    Set<Manufacture> manufactures;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    Set<Location> locations;

    String note;

    Boolean status;
}
