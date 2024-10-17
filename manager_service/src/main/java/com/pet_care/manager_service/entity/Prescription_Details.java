package com.pet_care.manager_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="prescription_details")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prescription_Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "note", columnDefinition = "text", nullable = true)
    String note;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    Medicine medicine;

}
