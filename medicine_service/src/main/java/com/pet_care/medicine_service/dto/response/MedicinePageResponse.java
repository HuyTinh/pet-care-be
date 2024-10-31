package com.pet_care.medicine_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicinePageResponse {
     List<MedicineResponse> medicines;
     int totalPages;
     long totalElements;
}
