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
@Table(name = "medicines")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Medicine {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    String name;

    int quantity;

    double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "caculation_unit_id")
    CaculationUnits caculationUnits;

    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    @JsonIgnore
    List<PrescriptionDetails> prescriptionDetails;

}
