package com.pet_care.warehouse_manager_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
//import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="medicines")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "manufacturing_date", columnDefinition = "DATE", nullable = false)
    LocalDate manufacturing_date;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    LocalDate expiry_date;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "note", columnDefinition = "Text", nullable = true)
    String note;

    @Column(name = "status", nullable = false)
    boolean status = true;
    
    @ManyToOne
    @JoinColumn(name = "caculation_unit_id")
    Caculation_Unit caculation_unit;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    Manufacturer manufacturer;
}
