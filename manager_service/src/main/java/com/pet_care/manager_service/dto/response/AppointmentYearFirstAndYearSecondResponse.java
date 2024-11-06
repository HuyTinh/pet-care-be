package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentYearFirstAndYearSecondResponse {
    Long month;
    String monthName;
    Long count_year_first;
    Long count_year_second;
    BigDecimal percent_year_first;
    BigDecimal percent_year_second;
}
