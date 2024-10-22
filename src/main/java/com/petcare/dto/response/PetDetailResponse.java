package com.petcare.dto.response;

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
public class PetDetailResponse
{

    long petId;

    String petName;

    String petAge;

    String petWeight;

    String petSpecies;

    String ownerName;

    List<PrescriptionResponse> prescriptionResponses;

    List<ServiceResponse> serviceResponses;

    double totalPriceInPetDetail;

}
