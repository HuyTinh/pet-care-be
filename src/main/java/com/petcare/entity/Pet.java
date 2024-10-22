package com.petcare.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "pets")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    String name;

    String age;

    String weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    Owner owner;

    @OneToMany(mappedBy = "pets", fetch = FetchType.EAGER)
//    @JsonIgnore
    List<Prescription> prescriptions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "species_id")
//    @JsonIgnore
    Species species;

}
