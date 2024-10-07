package com.pet_care.medicine_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String area;

    @JsonProperty("row_location")
    Integer rowLocation;

    @JsonProperty("column_location")
    Integer columnLocation;

    Boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medicine_id")
    Medicine medicine;
}