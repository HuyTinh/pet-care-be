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
public class ServiceYearFirstAndYearSecondResponse {
    Long month;
    String monthName;
    Long service_year_first;
    Long service_year_second;
    BigDecimal percent_year_first;
    BigDecimal percent_year_second;
}
