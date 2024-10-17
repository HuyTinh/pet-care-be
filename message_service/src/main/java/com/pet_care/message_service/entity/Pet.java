package com.pet_care.message_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pet implements Comparable<Pet> {
    Long id;
    String name;


    @Override
    public int compareTo(Pet o) {
        return Long.compare(this.id,o.id);
    }
}