package com.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "prescription")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescription{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    @Column(name = "create_date")
    LocalDate createDate;

    boolean status;

    String note; // bệnh gì

    @OneToMany (mappedBy = "prescription", fetch = FetchType.EAGER)
    List<PrescriptionDetails> prescriptionDetails;

    @ManyToOne()
    @JoinColumn(name = "pet_id")
    @JsonIgnore
    Pet pets;

}
