package com.pet_care.medicine_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @JsonProperty("date_import")
    Date dateImport;

    @JsonProperty("image_url")
    String imageUrl;

    Integer quantity;

    Double price;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    Set<CalculationUnit> calculationUnits;

    @ManyToOne()
    @JoinColumn(name = "manufacture_id")
    Manufacture manufacture;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    Set<Location> locations;

    String note;

    @Builder.Default
    MedicineStatus status = MedicineStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    MedicineTypes types;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;


    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
