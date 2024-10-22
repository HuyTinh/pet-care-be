package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="medicines")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    Set<Invoice_Medicine_Detail> invoice_medicine_detail;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    Set<Prescription_Details> prescription_details;
}
