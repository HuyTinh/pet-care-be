package com.pet_care.warehouse_manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="manufacturers")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "status", nullable = false)
    boolean status = true;

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer")
    Set<Medicine> medicines;
}
