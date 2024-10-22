package com.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "prescription_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

//    @Column(name = "medicine_id")
//    long medicineId;

    @Column(name = "note")
    String note;

    @Column(name = "quantity")
    int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription", nullable = false)
    @JsonIgnore
    Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine_id")
    Medicine medicine;
}
