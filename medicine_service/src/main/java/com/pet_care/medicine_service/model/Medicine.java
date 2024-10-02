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

    @OneToMany(mappedBy = "medicine", cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, orphanRemoval = true)
    Set<CalculationUnit> calculationUnits;

    @OneToMany(mappedBy = "medicine", cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, orphanRemoval = true)
    Set<Manufacture> manufactures;

    @OneToMany(mappedBy = "medicine", cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, orphanRemoval = true)
    Set<Location> locations;

    String note;

    Boolean status;
}
