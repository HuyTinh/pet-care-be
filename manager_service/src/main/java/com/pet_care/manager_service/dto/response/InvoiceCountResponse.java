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
public class InvoiceCountResponse {
    CustomerHomeDashboardResponse customers;
    AppointmentHomeDashboardResponse appointments;
    InvoiceHomeDashboardResponse invoices;
    Double total;
    String today_yesterday;
    Long month;
    Long year;
    Set<InvoiceOfMonthResponse> invoiceOfMonth;
    Set<InvoiceOfYearResponse> invoiceOfYears;
    Long year_first;
    Long year_second;
    Set<RevenueYearFirstAndYearSecondResponse> revenueYearFirstAndYearSeconds;
}
