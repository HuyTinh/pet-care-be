package com.petcare.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse
{

    long petId;

    String petName;

    String petAge;

    String petWeight;

    String petSpecies;

    String ownerName;

}
