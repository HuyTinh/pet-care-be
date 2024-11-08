package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineChartResponse {
    Long month;
    Long year;
    Set<MedicineOfMonthAndYearResponse> medicineOfMonthAndYear;
    Long year_first;
    Long year_second;
    Set<MedicineYearFirstAndYearSecondResponse> medicineYearFirstAndYearSecond;
}
