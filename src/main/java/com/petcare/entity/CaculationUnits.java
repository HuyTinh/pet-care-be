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
@Table(name = "caculation_units")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaculationUnits {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    String name;

    @OneToMany(mappedBy = "caculationUnits", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Medicine> medicine;

}
