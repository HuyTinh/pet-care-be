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
public class ServiceChartResponse {
    Long month;
    Long year;
    Set<ServiceOfMonthResponse> serviceOfMonth;
    Set<ServiceRevenueOfMonthResponse> serviceRevenueOfMonth;
    Long year_first;
    Long year_second;
    Set<ServiceYearFirstAndYearSecondResponse> serviceYearFirstAndYearSecond;
}
