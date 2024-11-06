package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentFromAndTodateResponse {
    LocalDate from_date;
    LocalDate to_date;
    Set<AppointmentChartResponse> appointment_chart_response;
    Long month;
    Long year;
    Set<AppointmentChartResponse> appointment_month_year;
    Long year_first;
    Long year_second;
    Set<AppointmentYearFirstAndYearSecondResponse> appointment_year_first_year_second_response;
}
