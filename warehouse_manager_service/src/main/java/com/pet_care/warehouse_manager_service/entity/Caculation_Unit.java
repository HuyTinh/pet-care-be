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
@Entity(name="caculation_units")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Caculation_Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "status", nullable = false)
    boolean status = true;

    @JsonIgnore
    @OneToMany(mappedBy = "caculation_unit")
    Set<Medicine> medicines;
}
