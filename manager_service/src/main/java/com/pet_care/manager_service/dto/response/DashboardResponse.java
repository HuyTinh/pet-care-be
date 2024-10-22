package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
public class DashboardResponse {
    CustomerHomeDashboardResponse customers;
    AppointmentHomeDashboardResponse appointments;
    InvoiceHomeDashboardResponse invoices;
    Set<PrescriptionHomeDashboardResponse> prescriptions;
}
