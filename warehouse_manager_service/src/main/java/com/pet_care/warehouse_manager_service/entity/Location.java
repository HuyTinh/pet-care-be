package com.pet_care.warehouse_manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="locations")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "area", nullable = false)
    String area;

    @Column(name = "row_location", nullable = false)
    int row_column;

    @Column(name = "column_location", nullable = false)
    int column_location;

    @Column(name = "status", nullable = false)
    boolean status = true;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    Set<Medicine> medicines;
}
