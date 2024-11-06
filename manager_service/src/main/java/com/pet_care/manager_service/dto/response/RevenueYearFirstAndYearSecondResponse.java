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
public class RevenueYearFirstAndYearSecondResponse {
    Long month;
    String monthName;
    Double revenue_year_first;
    Double revenue_year_second;
}
