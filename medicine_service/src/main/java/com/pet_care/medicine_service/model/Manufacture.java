package com.pet_care.medicine_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "manufactures")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Manufacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    Boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medicine_id")
    Medicine medicine;
}
